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

import static com.mongodb.client.model.Filters.*;


public class fetchStudentAttendanceStudentHome extends HttpServlet {


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
        Bson filter = and(eq("Participant_Email", session.getAttribute("user")), eq("PeriodWiseModified",true));
        Bson projection = Projections.fields(Projections.include("P1","P2", "P3", "Meeting_ID"),
                Projections.excludeId());

        MongoCursor<org.bson.Document> cursor = collection.find(filter).projection(projection).cursor();
        org.bson.Document data = null;
        JSONArray array = new JSONArray();
        try {
            while (cursor.hasNext()) {
                data = cursor.next();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", date);
                jsonObject.put("Meeting_ID", data.getString("Meeting_ID"));
                jsonObject.put("P1", data.get("P1"));
                jsonObject.put("P2", data.get("P2"));
                jsonObject.put("P3", data.get("P3"));
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