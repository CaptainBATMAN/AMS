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

    // public static void main(String[] args) {
    // String loginId = "test@faculty.ac.in";
    // String pwd = "dasd";

    // ConnectionString connectionString = new
    // ConnectionString("mongodb://127.0.0.1:27017");
    // MongoClient mongoClient = MongoClients.create(connectionString);
    // MongoDatabase database = mongoClient.getDatabase("university");
    // MongoCollection<org.bson.Document> collection =
    // database.getCollection("users");

    // Bson filter = eq("email", loginId);
    // Bson projection = Projections.fields(Projections.include("email", "password",
    // "user_role"),
    // Projections.excludeId());

    // String FetchedEmail = null;
    // String FetchedPassword = null;
    // String FetchedUser_role = null;
    // String redirectURL = "login.jsp";
    // MongoCursor<org.bson.Document> cursor =
    // collection.find(filter).projection(projection).cursor();
    // try {
    // while (cursor.hasNext()) {
    // org.bson.Document data = cursor.next();
    // FetchedEmail = data.getString("email");
    // System.out.println(FetchedEmail);
    // FetchedPassword = data.getString("password");
    // System.out.println(FetchedPassword);

    // FetchedUser_role = data.getString("user_role");
    // System.out.println(FetchedUser_role);

    // }
    // } finally {

    // cursor.close();
    // if (FetchedEmail == null && FetchedPassword == null && FetchedUser_role ==
    // null) {
    // System.out.println("bruh");
    // }
    // if (loginId.equals(FetchedEmail) && pwd.equals(FetchedPassword)) {
    // if (FetchedUser_role.equals("student")) {
    // System.out.println("ikkadiki ela vacchav");

    // } else if (FetchedUser_role.equals("faculty")) {
    // System.out.println("ikkadiki ela vacchav 2");

    // }
    // }

    // }
    // }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loginId = request.getParameter("emailId");
        String pwd = request.getParameter("pwd");

        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection("users");

        Bson filter = eq("email", loginId);
        Bson projection = Projections.fields(Projections.include("email", "password", "user_role"),
                Projections.excludeId());

        String FetchedEmail = null;
        String FetchedPassword = null;
        String FetchedUser_role = null;
        String redirectURL = "login.jsp";
        MongoCursor<org.bson.Document> cursor = collection.find(filter).projection(projection).cursor();
        try {
            while (cursor.hasNext()) {
                org.bson.Document data = cursor.next();
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
                    redirectURL = "./secure/studentHome.jsp";
                } else if (FetchedUser_role.equals("faculty")) {

                    HttpSession session = request.getSession();
                    session.setAttribute("user", loginId);
                    session.setAttribute("role", FetchedUser_role);
                    redirectURL = "./secure/facultyHome.jsp";
                }
            } else {
                request.setAttribute("status", "Wrong Credentials. Try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            response.sendRedirect(redirectURL);
        }
    }

}