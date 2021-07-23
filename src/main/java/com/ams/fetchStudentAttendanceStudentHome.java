package com.ams;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

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
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.bson.json.JsonMode;

public class fetchStudentAttendanceStudentHome extends HttpServlet {

    // public static void main(String[] args) {
    //     String collectionName = "db_24_05_2021";

    //     ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
    //     MongoClient mongoClient = MongoClients.create(connectionString);
    //     MongoDatabase database = mongoClient.getDatabase("university");
    //     MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

    //     Bson filter = eq("Participant_Email", "s160003@rguktsklm.ac.in");
    //     Bson projection = Projections.fields(Projections.include("Date", "Meeting_ID", "Participant_Email", "Duration"),
    //             Projections.excludeId());

    //     MongoCursor<org.bson.Document> cursor = collection.find(filter).projection(projection).cursor();
    //     System.out.println(cursor);
    //     org.bson.Document data = null;
    //     JSONArray array = new JSONArray();
    //     try {
    //         while (cursor.hasNext()) {
    //             data = cursor.next();
    //             JSONObject jsonObject = new JSONObject();
    //             jsonObject.put("Date", data.getString("Date"));
    //             jsonObject.put("Meeting_ID", data.getString("Meeting_ID"));
    //             jsonObject.put("Participant_Email", data.getString("Participant_Email"));
    //             jsonObject.put("Duration", data.getInteger("Duration"));
    //             array.add(jsonObject);
    //             System.out.println(data);
    //         }
    //     } finally {

    //         System.out.println(array);
    //         cursor.close();

    //     }
    // }

    public fetchStudentAttendanceStudentHome() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String date = request.getParameter("Date");
        String collectionName = "db_"+ date.replace("-","_");
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

        HttpSession session = request.getSession();
        Bson filter = eq("Participant_Email", session.getAttribute("user"));
        Bson projection = Projections.fields(Projections.include("Date","Meeting_ID", "Participant_Email", "Duration"),
                Projections.excludeId());

        MongoCursor<org.bson.Document> cursor = collection.find(filter).projection(projection).cursor();
        org.bson.Document data = null;
        JSONArray array = new JSONArray();
        try {
            while (cursor.hasNext()) {
                data = cursor.next();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Date", data.getString("Date"));
                jsonObject.put("Meeting_ID", data.getString("Meeting_ID"));
                jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                jsonObject.put("Duration", data.getInteger("Duration"));
                array.add(jsonObject);
            }
        } finally {

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(array);
            out.flush();
            cursor.close();
        }
    }
}