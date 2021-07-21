package com.ams;

import javax.print.Doc;
import javax.swing.text.Document;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.conversions.Bson;

public class mongoDBManager {

    // public void addNewUser() {
    //     ConnectionString connectionString = new ConnectionString("mongodb://locahost:27017");
    //     MongoClient mongoClient = MongoClients.create(connectionString);
    //     MongoDatabase database = mongoClient.getDatabase("university");
    //     MongoCollection<org.bson.Document> collection = database.getCollection("users");
    // }

    public void fetchAttendanceReportForStudentHome() {
        ConnectionString connectionString = new ConnectionString("mongodb://locahost:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection("users");

        //filer to apply filters
        // Bson filter = Filters.and(Filters.gt("FromTime", "9:00"), Filters.lt("toTime","12:00"), Filter.eq("gmeetCode","gmeetCode"));
        // // collection.find(filter);
        
    }
}
