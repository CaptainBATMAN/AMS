package com.ams;

import java.io.Console;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.Document;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Projections;
import com.mongodb.client.FindIterable;

import org.bson.conversions.Bson;

public class loginAuth extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loginId = request.getParameter("emailId");
        String pwd = request.getParameter("pwd");

        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoDatabase database = mongoClient.getDatabase("users");

        MongoCollection<org.bson.Document> collection = database.getCollection("faculty");
        Bson filter = eq("email", loginId);
        long countInFaculty = collection.countDocuments(filter);
        Bson projection = null;
        if (countInFaculty == 0) {
            collection = database.getCollection("students");
            projection = Projections.fields(Projections.include("email", "password", "user_role", "class"),
                    Projections.excludeId());
        } else {
            projection = Projections.fields(Projections.include("email", "password", "user_role", "class", "subject"),
                    Projections.excludeId());
        }

        String FetchedEmail = null;
        String FetchedPassword = null;
        String FetchedUser_role = null;
        String redirectURL = "login.jsp";

        MongoCursor<org.bson.Document> cursor = collection.find(filter).projection(projection).cursor();

        org.bson.Document data = null;
        try {
            while (cursor.hasNext()) {
                data = cursor.next();
                FetchedEmail = data.getString("email");
                FetchedPassword = data.getString("password");
                FetchedUser_role = data.getString("user_role");
            }
        } finally {

            cursor.close();

            if (FetchedEmail == null && FetchedPassword == null && FetchedUser_role == null) {
                request.setAttribute("status", "Wrong Credentials. Try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            if (loginId.equals(FetchedEmail) && pwd.equals(FetchedPassword)) {
                if (FetchedUser_role.equals("student")) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", loginId);
                    session.setAttribute("role", FetchedUser_role);
                    session.setAttribute("class", data.getString("class"));
                    session.setAttribute("name", data.getString("username"));
                    redirectURL = "./secure/studentHome.jsp";
                } else if (FetchedUser_role.equals("faculty")) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", loginId);
                    session.setAttribute("role", FetchedUser_role);
                    session.setAttribute("class", data.get("class"));
                    session.setAttribute("subject", data.get("subject"));
                    redirectURL = "./secure/facultyHomeFetch.jsp";
                } else if (FetchedUser_role.equals("admin")) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", loginId);
                    session.setAttribute("role", FetchedUser_role);
                    session.setAttribute("class", data.get("class"));
                    redirectURL = "./secure/adminHome.jsp";
                }
            } else {
                request.setAttribute("status", "Wrong Credentials. Try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            response.sendRedirect(redirectURL);
        }
    }

}