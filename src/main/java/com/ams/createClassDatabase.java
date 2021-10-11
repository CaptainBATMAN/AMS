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

public class createClassDatabase extends HttpServlet {
    public createClassDatabase() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String className = request.getParameter("className");
        className = className.toLowerCase().replace("-", "_");
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoIterable<String> databaseList = mongoClient.listDatabaseNames();
        Object data = null;
        ArrayList<String> dbList = new ArrayList<>();
        MongoCursor cursor = databaseList.cursor();
        while (cursor.hasNext()) {
            data = cursor.next();
            if (data.equals("admin") || data.equals("config") || data.equals("local")) {
                continue;
            }
            dbList.add((String) data);
        }
        boolean exists = false;
        for (String db : dbList) {
            if (db.equals(className)) {
                JSONObject report = new JSONObject();
                report.put("msg", "Database Already Exists...");
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(report);
                out.flush();
                exists = true;
                break;
            }
        }
        if (exists == false) {
            MongoDatabase database = mongoClient.getDatabase(className);
            database.createCollection(className + "_students");
            database.createCollection(className + "_subjects");
        }

    }
}
