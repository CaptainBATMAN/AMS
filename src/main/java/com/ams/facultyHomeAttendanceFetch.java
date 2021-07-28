package com.ams;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.ConnectionString;
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

public class facultyHomeAttendanceFetch extends HttpServlet {

    // public static void main(String[] args) {
    // // * get params from request
    // String date = "11-11-2020";
    // String fromTime = "11:00:00";
    // String toTime = "12:30:00";
    // String meetingID = "ATCBCNNUPO";

    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    // Date fromTimeDate = null;
    // try {
    // fromTimeDate = simpleDateFormat.parse(fromTime);
    // } catch (ParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // Date toTimeDate = null;
    // try {
    // toTimeDate = simpleDateFormat.parse(toTime);
    // } catch (ParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // System.out.println(fromTimeDate);
    // System.out.println(toTimeDate);
    // // * creating DB instance
    // String collectionName = "db_" + date.replace("-", "_");
    // ConnectionString connectionString = new
    // ConnectionString("mongodb://127.0.0.1:27017");
    // MongoClient mongoClient = MongoClients.create(connectionString);
    // MongoDatabase database = mongoClient.getDatabase("university");
    // MongoCollection<org.bson.Document> collection =
    // database.getCollection(collectionName);
    // System.out.println(collectionName);
    // // * filtering data and fields needed.

    // Bson filter;
    // System.out.println("filter");
    // if(meetingID.equals("")){
    // filter = eq("Class", "CSE-05");
    // System.out.println("into if");
    // }
    // else{
    // System.out.println("into else");

    // filter = and(eq("Meeting_ID", meetingID),eq("Class","CSE-05"));
    // }
    // System.out.println(filter);
    // Bson projection = Projections.fields(Projections.include("Meeting_ID",
    // "Participant_Email", "Duration",
    // "Start_Time", "End_Time", "Class", "Subject"), Projections.excludeId());

    // MongoCursor<org.bson.Document> cursor =
    // collection.find(filter).projection(projection).cursor();
    // org.bson.Document data = null;
    // JSONArray array = new JSONArray();

    // try {

    // // * traversing through all the documents
    // while (cursor.hasNext()) {
    // data = cursor.next();

    // Date studentStartTimeDate =
    // simpleDateFormat.parse(data.getString("Start_Time"));
    // Date fromStartTimeDate = simpleDateFormat.parse(data.getString("End_Time"));

    // if ((studentStartTimeDate.getTime() >= fromTimeDate.getTime()
    // && studentStartTimeDate.getTime() <= toTimeDate.getTime())
    // && (fromStartTimeDate.getTime() >= fromTimeDate.getTime()
    // && fromStartTimeDate.getTime() <= toTimeDate.getTime())) {

    // JSONObject jsonObject = new JSONObject();
    // jsonObject.put("Start_Time", data.getString("Start_Time"));
    // jsonObject.put("End_Time", data.getString("End_Time"));
    // jsonObject.put("Meeting_ID", data.getString("Meeting_ID"));
    // jsonObject.put("Participant_Email", data.getString("Participant_Email"));
    // jsonObject.put("Duration", data.getInteger("Duration"));
    // jsonObject.put("Class", data.getString("Class"));
    // jsonObject.put("Subject", data.getString("Subject"));
    // array.add(jsonObject);

    // }
    // // * filter data here using condition statements

    // // * gathering required data into JSON object and then pushing

    // }
    // } catch (ParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } finally {

    // System.out.println(array);
    // // * Sending JSON array as a response
    // // PrintWriter out = response.getWriter();
    // // response.setContentType("application/json");
    // // response.setCharacterEncoding("UTF-8");
    // // out.print(array);
    // // out.flush();
    // cursor.close();
    // }
    // }

    public facultyHomeAttendanceFetch() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // * get params from request
        String date = request.getParameter("Date");
        String subject = request.getParameter("subject");
        String className = request.getParameter("className");

        // * creating DB instance
        String collectionName = "db_" + date.replace("-", "_");
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

        // * filtering data and fields needed.
        Bson newFilter;
        newFilter = and(eq("PeriodWiseModified", true));
        Bson projection = Projections.fields(Projections.include("Participant_Email", "Meeting_ID", "P1", "P2", "P3"),
                Projections.excludeId());

                MongoCursor<org.bson.Document> cursor = collection.find(newFilter).projection(projection).cursor();
        org.bson.Document data = null;
        JSONArray array = new JSONArray();

        try {

            // * traversing through all the documents
            while (cursor.hasNext()) {
                data = cursor.next();
                JSONObject jsonObject = new JSONObject();

                Bson p1Filter = and(eq("P1.Subject", subject), eq("P1.Class", className));
                Bson p2Filter = and(eq("P2.Subject", subject), eq("P2.Class", className));
                Bson p3Filter = and(eq("P3.Subject", subject), eq("P3.Class", className));

                long p1Count, p2Count, p3Count;
                p1Count = collection.countDocuments(p1Filter);
                p2Count = collection.countDocuments(p2Filter);
                p3Count = collection.countDocuments(p3Filter);


                if (p1Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P1", Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P1", Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P1", Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P1", Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
                if (p2Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P2", Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P2", Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P2", Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P2", Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
                if (p3Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P3", Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P3", Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P3", Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P3", Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
            }
        } finally {
            // * Sending JSOM array as a response

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(array);
            out.flush();
            cursor.close();
        }
    }
}
