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
import org.json.simple.JSONObject;

public class createStudentAccount extends HttpServlet {
    public createStudentAccount() {
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
       

        String studentName = request.getParameter("studentName");
        String studentEmail = request.getParameter("studentEmail");
        String studentClass = request.getParameter("studentClass");
        String stuPasswd = request.getParameter("stuPasswd");
        String userRole = "student";

        MongoDatabase database = mongoClient.getDatabase("users");
        MongoCollection studentCollection = database.getCollection("students");
        Document document = new Document("email", studentEmail).append("username", studentName)
                .append("user_role", userRole).append("class", studentClass).append("password", stuPasswd);
        studentCollection.insertOne(document);
    }
}
