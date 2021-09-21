package com.ams;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;
import java.lang.Object;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.*;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
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

public class adminHomeFetch extends HttpServlet {

    public adminHomeFetch() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fromDateMillis = request.getParameter("fromDateMillis");
        String toDateMillis = request.getParameter("toDateMillis");
        String className = request.getParameter("className");

        // ! Change minDuration here
        int minDuration = 0;

        // ! Connecting to MongoDB server on port 27017..
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        // ConnectionString connectionString = new
        // ConnectionString("mongodb+srv://admin:Batman123Pass@amscluster.osjva.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        // MongoClientSettings settings =
        // MongoClientSettings.builder().applyConnectionString(connectionString).build();
        // MongoClient mongoClient = MongoClients.create(settings);

        // ! getting subjectList from class_section_subjects
        String subjectDbName = className.toLowerCase().replace("-", "_");
        MongoDatabase subjectDatabase = mongoClient.getDatabase(subjectDbName);
        MongoCollection<org.bson.Document> subjectsCollection = subjectDatabase
                .getCollection(subjectDbName + "_subjects");
        Bson subjectListFilter = eq("subjectList", true);
        Bson subjectListProjection = Projections.fields(Projections.include("subjects"), Projections.excludeId());
        MongoCursor<org.bson.Document> subjectCursor = subjectsCollection.find(subjectListFilter)
                .projection(subjectListProjection).cursor();
        long subjectCount = subjectsCollection.countDocuments(subjectListFilter);
        ArrayList<String> subjectArray = new ArrayList<String>();
        try {
            while (subjectCursor.hasNext()) {
                subjectArray = (ArrayList<String>) subjectCursor.next().get("subjects");
            }
            subjectCursor.close();
        } finally {
        }

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
        try {
            while (emailCursor.hasNext()) {
                String emailID = emailCursor.next().getString("Student_Email").toLowerCase();
                studentEmailArray[emailArrayIndex] = emailID;
                emailArrayIndex = emailArrayIndex + 1;
            }
            emailCursor.close();
        } finally {

            JSONArray array = new JSONArray();
            int i = 0;
            for (String studentEmail : studentEmailArray) {

                // ! Creating JSON object and initializing it.
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Student_Email", studentEmail);
                for (int j = 0; j < subjectArray.size(); j++) {
                    jsonObject.put(subjectArray.get(j), 0);
                }

                // ! initializing total no of classes and classes that the student attended

                jsonObject.put("totalClasses", 0);
                jsonObject.put("classesAttended", 0);

                for (long start = Long.parseLong(fromDateMillis); start <= Long.parseLong(toDateMillis); start = start
                        + 86400000) {

                    Date date = new Date(start);
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String currentDate = format.format(date);

                    String db = className.toLowerCase().replace("-", "_");
                    String collectionName = db + "_" + currentDate.replace("-", "_");

                    MongoDatabase database = mongoClient.getDatabase(db);
                    MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

                    Bson newFilter;
                    newFilter = and(eq("PeriodWiseModified", true), eq("Participant_Email", studentEmail));
                    Bson projection = Projections.fields(Projections.include("Participant_Email", "Meeting_ID", "P1",
                            "P2", "P3", "Duration", "Class", "Subject"), Projections.excludeId());
                    MongoCursor<org.bson.Document> cursor = collection.find(newFilter).projection(projection).cursor();
                    org.bson.Document data = null;
                    while (cursor.hasNext()) {
                        data = cursor.next();

                        Bson p1Filter = and(ne("P1", null), eq("P1.Class", className));
                        Bson p2Filter = and(ne("P2", null), eq("P2.Class", className));
                        Bson p3Filter = and(ne("P3", null), eq("P3.Class", className));

                        long p1Count, p2Count, p3Count;
                        p1Count = collection.countDocuments(p1Filter);
                        p2Count = collection.countDocuments(p2Filter);
                        p3Count = collection.countDocuments(p3Filter);

                        if (p1Count > 0) {

                            int totalClassVal = Integer.parseInt(jsonObject.get("totalClasses").toString());
                            totalClassVal = totalClassVal + 1;
                            jsonObject.remove("totalClasses");
                            jsonObject.put("totalClasses", totalClassVal);

                            if (data.get("P1", Document.class).getInteger("Duration") > minDuration) {
                                String sub = data.get("P1", Document.class).getString("Subject");
                                int val = Integer.parseInt(jsonObject.get(sub).toString());
                                val = val + 1;
                                jsonObject.remove(sub);
                                jsonObject.put(sub, val);

                                int attendenClassVal = Integer.parseInt(jsonObject.get("classesAttended").toString());
                                attendenClassVal = attendenClassVal + 1;
                                jsonObject.remove("classesAttended");
                                jsonObject.put("classesAttended", attendenClassVal);

                            }
                        }

                        if (p2Count > 0) {

                            int totalClassVal = Integer.parseInt(jsonObject.get("totalClasses").toString());
                            totalClassVal = totalClassVal + 1;
                            jsonObject.remove("totalClasses");
                            jsonObject.put("totalClasses", totalClassVal);

                            if (data.get("P2", Document.class).getInteger("Duration") > minDuration) {
                                String sub = data.get("P2", Document.class).getString("Subject");
                                int val = Integer.parseInt(jsonObject.get(sub).toString());
                                val = val + 1;
                                jsonObject.remove(sub);
                                jsonObject.put(sub, val);

                                int attendenClassVal = Integer.parseInt(jsonObject.get("classesAttended").toString());
                                attendenClassVal = attendenClassVal + 1;
                                jsonObject.remove("classesAttended");
                                jsonObject.put("classesAttended", attendenClassVal);
                            }
                        }

                        if (p3Count > 0) {

                            int totalClassVal = Integer.parseInt(jsonObject.get("totalClasses").toString());
                            totalClassVal = totalClassVal + 1;
                            jsonObject.remove("totalClasses");
                            jsonObject.put("totalClasses", totalClassVal);

                            if (data.get("P3", Document.class).getInteger("Duration") > minDuration) {
                                String sub = data.get("P3", Document.class).getString("Subject");
                                int val = Integer.parseInt(jsonObject.get(sub).toString());
                                val = val + 1;
                                jsonObject.remove(sub);
                                jsonObject.put(sub, val);

                                int attendenClassVal = Integer.parseInt(jsonObject.get("classesAttended").toString());
                                attendenClassVal = attendenClassVal + 1;
                                jsonObject.remove("classesAttended");
                                jsonObject.put("classesAttended", attendenClassVal);
                            }
                        }
                    }

                }
                array.add(jsonObject);
            }

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(array);
            out.flush();
        }
    }
}
