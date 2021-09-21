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
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
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
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class createFacultyAccount extends HttpServlet {

    public createFacultyAccount() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        // ConnectionString connectionString = new
        // ConnectionString("mongodb+srv://admin:Batman123Pass@amscluster.osjva.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        // MongoClientSettings settings =
        // MongoClientSettings.builder().applyConnectionString(connectionString).build();
        // MongoClient mongoClient = MongoClients.create(settings);

        String facultyName = request.getParameter("facultyName");
        String facultyEmail = request.getParameter("facultyEmail");
        String facClassString = request.getParameter("facultyClass");
        String facSubjectString = request.getParameter("facultySubject");

        ArrayList<String> facClassArray = new ArrayList<>(Arrays.asList(facClassString.split(" ")));
        ArrayList<String> facSubjectArray = new ArrayList<>(Arrays.asList(facSubjectString.split(" ")));
        String facPasswd = request.getParameter("facPasswd");
        String userRole = "faculty";

        MongoDatabase database = mongoClient.getDatabase("users");
        MongoCollection facultyCollection = database.getCollection("faculty");
        Document document = new Document("email", facultyEmail).append("username", facultyName)
                .append("user_role", userRole).append("class", facClassArray).append("subject", facSubjectArray)
                .append("password", facPasswd);
        facultyCollection.insertOne(document);
    }
}
