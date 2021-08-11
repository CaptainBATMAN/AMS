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

public class getsubjectsAdminFetch extends HttpServlet {
    // public static void main(String[] args) {
    //     String className = "CSE-1B";
    //     // ! Connecting to MongoDB server on port 27017..
    //     ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
    //     MongoClient mongoClient = MongoClients.create(connectionString);

    //     // ! getting subjectList from class_section_subjects
    //     String subjectDbName = className.toLowerCase().replace("-", "_");
    //     MongoDatabase subjectDatabase = mongoClient.getDatabase(subjectDbName);
    //     MongoCollection<org.bson.Document> subjectsCollection = subjectDatabase
    //             .getCollection(subjectDbName + "_subjects");
    //     Bson subjectListFilter = eq("subjectList", true);
    //     Bson subjectListProjection = Projections.fields(Projections.include("subjects"), Projections.excludeId());
    //     MongoCursor<org.bson.Document> subjectCursor = subjectsCollection.find(subjectListFilter)
    //             .projection(subjectListProjection).cursor();
    //     long subjectCount = subjectsCollection.countDocuments(subjectListFilter);

    //     if (subjectCount > 0) {

    //         ArrayList<String> subjectArray = new ArrayList<String>();
    //         try {
    //             while (subjectCursor.hasNext()) {
    //                 subjectArray = (ArrayList<String>) subjectCursor.next().get("subjects");
    //             }
    //             subjectCursor.close();
    //         } finally {
    //             System.out.println(subjectArray);
    //         }
    //     } else {
    //         System.out.println("error");
    //     }
    // }

    public getsubjectsAdminFetch() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String className = request.getParameter("className");
        // ! Connecting to MongoDB server on port 27017..
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        // ! getting subjectList from class_section_subjects
        String subjectDbName = className.toLowerCase().replace("-", "_");
        MongoDatabase subjectDatabase = mongoClient.getDatabase(subjectDbName);
        MongoCollection<org.bson.Document> subjectsCollection = subjectDatabase
                .getCollection(subjectDbName + "_subjects");
        Bson subjectListFilter = eq("subjectList", true);
        Bson subjectListProjection = Projections.fields(Projections.include("subjects"), Projections.excludeId());
        MongoCursor<org.bson.Document> subjectCursor = subjectsCollection.find(subjectListFilter)
                .projection(subjectListProjection).cursor();
        ArrayList<String> subjectArray = new ArrayList<String>();
        JSONArray array = new JSONArray();
        long subjectCount = subjectsCollection.countDocuments(subjectListFilter);

        if (subjectCount > 0) {
            try {
                while (subjectCursor.hasNext()) {
                    subjectArray = (ArrayList<String>) subjectCursor.next().get("subjects");
                }
                subjectCursor.close();
                
                for(String subject : subjectArray){
                    array.add(subject);
                }
            } finally {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(array);
                out.flush();
            }
        }
        else{
            JSONObject failure = new JSONObject();
            failure.put("msg", "No subject list found..");
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(failure);
            out.flush();
        }
    }
}
