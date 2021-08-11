package com.ams;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.time.Duration;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.ConnectionString;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class facultyHomeAttendanceUpdate extends HttpServlet {

    public facultyHomeAttendanceUpdate() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ! get params from request
        String date = request.getParameter("Date");
        String fromTimeShort = request.getParameter("fromTime");
        String toTimeShort = request.getParameter("toTime");
        String subject = request.getParameter("subject");
        String className = request.getParameter("className");
        String meetingID = request.getParameter("Meeting_ID");
        String period = request.getParameter("period");
        
        String fromTime = fromTimeShort + ":00";
        String toTime = toTimeShort + ":00";


        HttpSession session = request.getSession();

        // ! converting Time strings to SimpleDateFormat Objects
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date classStartTime = null;
        try {
            classStartTime = simpleDateFormat.parse(fromTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date classEndTime = null;
        try {
            classEndTime = simpleDateFormat.parse(toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ! to find total duration of class
        int totalDurationOfClass = (int) ((classEndTime.getTime() - classStartTime.getTime()) / 1000);

        // ! Creating Database client.
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        // ! connecting to class DB and getting email list from class_section_students
        String dbName = className.toLowerCase().replace("-", "_");
        MongoDatabase modifiedDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<org.bson.Document> classStudentsCollection = modifiedDatabase
                .getCollection(dbName + "_students");

        Bson emailOnlyFilter = eq("Class", className);
        Bson emailOnlyProjection = Projections.fields(Projections.include("Student_Email"), Projections.excludeId());

        MongoCursor<org.bson.Document> emailCursor = classStudentsCollection.find(emailOnlyFilter)
                .projection(emailOnlyProjection).cursor();
        long emailCount = classStudentsCollection.countDocuments(emailOnlyFilter);
        String[] studentEmailArray = new String[(int) emailCount];
        int emailArrayIndex = 0;
        int totalDurationOfStudent = 0;
        int durationOfStudent = 0;
        try {
            while (emailCursor.hasNext()) {
                String emailID = emailCursor.next().getString("Student_Email").toLowerCase();
                studentEmailArray[emailArrayIndex] = emailID;
                emailArrayIndex = emailArrayIndex + 1;
            }
            emailCursor.close();
        } finally {

            // ! DB to search for the data
            String collectionName = "db_" + date.replace("-", "_");
            MongoDatabase database = mongoClient.getDatabase("university");
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);


            for (String email : studentEmailArray) {

                // ! filter to get all the entries for the given meeting Id and email
                Bson studentFilter = and(eq("Meeting_ID", meetingID), eq("Participant_Email", email),
                        eq("PeriodWiseModified", null));
                Bson studentProjection = Projections.fields(
                        Projections.include("Meeting_ID", "Participant_Email", "Duration", "Start_Time", "End_Time"),
                        Projections.excludeId());

                MongoCursor<org.bson.Document> studentCursor = collection.find(studentFilter)
                        .projection(studentProjection).cursor();

                try {
                    totalDurationOfStudent = 0;

                    while (studentCursor.hasNext()) {

                        org.bson.Document studentData = studentCursor.next();

                        Date studentStartTime = simpleDateFormat.parse(studentData.getString("Start_Time"));
                        Date studentEndTime = simpleDateFormat.parse(studentData.getString("End_Time"));

                        // ! Case-1 if student was already logged in before the class started..
                        if ((studentStartTime.getTime() <= classStartTime.getTime())
                                && ((studentEndTime.getTime() >= classStartTime.getTime()
                                        && studentEndTime.getTime() <= classEndTime.getTime()))) {
                            durationOfStudent = (int) ((studentEndTime.getTime() - classStartTime.getTime()) / 1000);
                            totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
                        }

                        // ! Case-2 if the student stayed even after the class Ended..

                        if ((studentEndTime.getTime() >= classEndTime.getTime())
                                && ((studentStartTime.getTime() >= classStartTime.getTime())
                                        && studentStartTime.getTime() <= classEndTime.getTime())) {
                            durationOfStudent = (int) ((classEndTime.getTime() - studentStartTime.getTime()) / 1000);
                            totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
                        }

                        // ! Case-3 Student joined in time and exited in time.
                        if ((studentStartTime.getTime() >= classStartTime.getTime()
                                && studentStartTime.getTime() <= classEndTime.getTime())
                                && (studentEndTime.getTime() >= classStartTime.getTime()
                                        && studentEndTime.getTime() <= classEndTime.getTime())) {
                            durationOfStudent = studentData.getInteger("Duration");
                            totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    String Meeting_ID = meetingID;
                    // ! this will be helpful if the student is logged in from two devices at the
                    // ! same time or he used screen share to present his screen.
                    if (totalDurationOfStudent > totalDurationOfClass) {
                        totalDurationOfStudent = totalDurationOfClass;
                    }

                    // ! Period object, the important part..
                    JSONObject subJsonObject = new JSONObject();
                    subJsonObject.put("Meeting_ID", Meeting_ID);
                    subJsonObject.put("Class", className);
                    String classTimings = fromTimeShort + " to " + toTimeShort;
                    subJsonObject.put("Class_Timings", classTimings);
                    subJsonObject.put("Subject", subject);
                    subJsonObject.put("Duration", totalDurationOfStudent);
                    subJsonObject.put("Modified_by",session.getAttribute("user") );

                    // ! db and collection to insert modified data
                    String modifiedCollectionName = dbName + "_" + date.replace("-", "_");
                    boolean collectionExists = mongoClient.getDatabase(dbName).listCollectionNames()
                    .into(new ArrayList<String>()).contains(modifiedCollectionName);
                    if (collectionExists == false) {
                        modifiedDatabase.createCollection(modifiedCollectionName);
                        
                    }
                    MongoCollection<org.bson.Document> modifiedCollection = modifiedDatabase.getCollection(modifiedCollectionName);
                    System.out.println("acquired collection");
                    
                    // ! To check if the PeriodWiseModified document exist in the DB..
                    Bson pwmFilter = and(eq("Participant_Email", email),
                    eq("PeriodWiseModified", true));
                    long pwmCount = modifiedCollection.countDocuments(pwmFilter);
                    if (pwmCount == 1) {                        
                        Bson periodUpdate = Updates.set(period, subJsonObject);
                        UpdateOptions options = new UpdateOptions().upsert(true);
                        modifiedCollection.updateOne(pwmFilter, periodUpdate, options);
                        
                    } else {                        
                        Document document = new Document("Participant_Email", email).append("PeriodWiseModified", true).append(period, subJsonObject);
                        modifiedCollection.insertOne(document);
                    }
                    studentCursor.close();
                }
            }

            // ! to acknowledge user that the data is updated successfully..
            JSONObject success = new JSONObject();
            success.put("msg", "Student records updated successfully...");
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(success);
            out.flush();
        }
    }

}