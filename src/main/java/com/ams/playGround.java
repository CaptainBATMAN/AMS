
// ! This file doesn't belong to the project. this file is for testing ..

package com.ams;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.time.Duration;
import java.time.Period;
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

public class playGround {

    // ! Faculty Update

    public static void main(String[] args) {

        // ! params that are usually taken from request
        String date = "20-07-2021";
        String fromTimeShort = "12:00";
        String toTimeShort = "13:30";
        String subject = "AI";
        String className = "CSE-3D";
        String period = "P3";
        String meetingID = "dadsa";

        // ! Just converting TimeShort to Time string
        String fromTime = fromTimeShort + ":00";
        String toTime = toTimeShort + ":00";

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
                    // subJsonObject.put("Modified By:", );

                    // ! db and collection to insert modified data
                    String modifiedCollectionName = dbName + "_" + date.replace("-", "_");
                    boolean collectionExists = mongoClient.getDatabase(dbName).listCollectionNames()
                            .into(new ArrayList<String>()).contains(modifiedCollectionName);
                    if (collectionExists == false) {
                        System.out.println("Created Collection!");
                        modifiedDatabase.createCollection(modifiedCollectionName);

                    }
                    MongoCollection<org.bson.Document> modifiedCollection = modifiedDatabase
                            .getCollection(modifiedCollectionName);
                    System.out.println("acquired collection");

                    // ! To check if the PeriodWiseModified document exist in the DB..
                    Bson pwmFilter = and(eq("Meeting_ID", meetingID), eq("Participant_Email", email),
                            eq("PeriodWiseModified", true));
                    long pwmCount = modifiedCollection.countDocuments(pwmFilter);
                    if (pwmCount == 1) {
                        System.out.println("updated existing doc");

                        Bson periodUpdate = Updates.set(period, subJsonObject);
                        UpdateOptions options = new UpdateOptions().upsert(true);
                        modifiedCollection.updateOne(pwmFilter, periodUpdate, options);

                    } else {

                        System.out.println("created new doc");
                        Document document = new Document("Participant_Email", email).append("Meeting_ID", Meeting_ID)
                                .append("PeriodWiseModified", true).append(period, subJsonObject);
                        modifiedCollection.insertOne(document);
                    }

                    studentCursor.close();
                }
            }
        }

    }

    // ! Student Fetch
    // public static void main(String[] args) {

    // // * get params from request
    // String date = "11-11-2020";
    // String subject = "USP";
    // String className = "CSE-05";
    // String meetingID = "";

    // // * creating DB instance
    // String collectionName = "db_" + date.replace("-", "_");
    // ConnectionString connectionString = new
    // ConnectionString("mongodb://127.0.0.1:27017");
    // MongoClient mongoClient = MongoClients.create(connectionString);
    // MongoDatabase database = mongoClient.getDatabase("university");
    // MongoCollection<org.bson.Document> collection =
    // database.getCollection(collectionName);

    // // * filtering data and fields needed.
    // Bson newFilter;
    // newFilter = and(eq("PeriodWiseModified", true));
    // Bson projection = Projections.fields(Projections.include("Participant_Email",
    // "Meeting_ID", "P1", "P2", "P3"),
    // Projections.excludeId());

    // long count = collection.countDocuments(newFilter);
    // System.out.println(count);
    // MongoCursor<org.bson.Document> cursor =
    // collection.find(newFilter).projection(projection).cursor();
    // org.bson.Document data = null;
    // JSONArray array = new JSONArray();

    // try {``

    // // * traversing through all the documents
    // while (cursor.hasNext()) {
    // data = cursor.next();
    // JSONObject jsonObject = new JSONObject();

    // Bson p1Filter = and(eq("P1.Subject", subject), eq("P1.Class", className));
    // Bson p2Filter = and(eq("P2.Subject", subject), eq("P2.Class", className));
    // Bson p3Filter = and(eq("P3.Subject", subject), eq("P3.Class", className));

    // long p1Count, p2Count, p3Count;
    // p1Count = collection.countDocuments(p1Filter);
    // p2Count = collection.countDocuments(p2Filter);
    // p3Count = collection.countDocuments(p3Filter);
    // System.out.println(p1Count);
    // System.out.println(p2Count);
    // System.out.println(p3Count);

    // if (p1Count > 0) {
    // jsonObject.put("Participant_Email", data.getString("Participant_Email"));
    // jsonObject.put("Meeting_ID", data.get("P1",
    // Document.class).getString("Meeting_ID"));
    // jsonObject.put("Class", data.get("P1", Document.class).getString("Class"));
    // jsonObject.put("Subject", data.get("P1",
    // Document.class).getString("Subject"));
    // jsonObject.put("Duration", data.get("P1",
    // Document.class).getInteger("Duration"));
    // array.add(jsonObject);
    // }
    // if (p2Count > 0) {
    // jsonObject.put("Participant_Email", data.getString("Participant_Email"));
    // jsonObject.put("Meeting_ID", data.get("P2",
    // Document.class).getString("Meeting_ID"));
    // jsonObject.put("Class", data.get("P2", Document.class).getString("Class"));
    // jsonObject.put("Subject", data.get("P2",
    // Document.class).getString("Subject"));
    // jsonObject.put("Duration", data.get("P2",
    // Document.class).getInteger("Duration"));
    // array.add(jsonObject);
    // }
    // if (p3Count > 0) {
    // jsonObject.put("Participant_Email", data.getString("Participant_Email"));
    // jsonObject.put("Meeting_ID", data.get("P3",
    // Document.class).getString("Meeting_ID"));
    // jsonObject.put("Class", data.get("P3", Document.class).getString("Class"));
    // jsonObject.put("Subject", data.get("P3",
    // Document.class).getString("Subject"));
    // jsonObject.put("Duration", data.get("P3",
    // Document.class).getInteger("Duration"));
    // array.add(jsonObject);
    // }
    // }
    // } finally {
    // // * Sending JSOM array as a response
    // System.out.println(array);
    // cursor.close();
    // }
    // }

    // ! Get Faculty Details

    // public static void main(String[] args) {

    // ConnectionString connectionString = new
    // ConnectionString("mongodb://127.0.0.1:27017");
    // MongoClient mongoClient = MongoClients.create(connectionString);
    // MongoDatabase database = mongoClient.getDatabase("university");
    // MongoCollection<org.bson.Document> collection =
    // database.getCollection("users");

    // // ! remember to get user attribute from session. to check if session exists.

    // Bson filter = and(eq("email", "test@faculty.ac.in"));
    // Bson projection = Projections.fields(Projections.include("class", "subject"),
    // Projections.excludeId());

    // MongoCursor<org.bson.Document> cursor =
    // collection.find(filter).projection(projection).cursor();
    // org.bson.Document data = null;
    // JSONArray array = new JSONArray();
    // try {
    // while (cursor.hasNext()) {
    // data = cursor.next();
    // ArrayList<String> classes = (ArrayList<String>) data.get("class");
    // ArrayList<String> subjects = (ArrayList<String>) data.get("subject");
    // ArrayList<String> checkNull = new ArrayList<String>();
    // System.out.println(checkNull.size());
    // checkNull.add("initial");
    // if (checkNull.size() == 0) {
    // checkNull.add("added");
    // }else{
    // checkNull.add("Not added");
    // }
    // System.out.println(checkNull.size());
    // System.out.println(checkNull);

    // System.out.println(classes);
    // System.out.println(subjects);
    // }
    // } finally {

    // // PrintWriter out = response.getWriter();
    // // response.setContentType("application/json");
    // // response.setCharacterEncoding("UTF-8");
    // // out.print(array);
    // // out.flush();
    // cursor.close();
    // }
    // }

    // ! Faculty Fetch

    // public static void main(String[] args) {

    // }

}
