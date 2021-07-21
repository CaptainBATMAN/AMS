package com.ams;

import java.io.IOException;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

public class fetchStudentAttendanceStudentHome {

    public void fetchAttendanceReportForStudentHome() {
        String collectionName = "24-05-2021";
        
        ConnectionString connectionString = new ConnectionString("mongodb://locahost:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

        Bson filter = eq("email", "s00000@rguktsklm.ac.in");
        MongoCursor<Document> cursor = collection.find(filter).cursor();
        int documentCount = 0;
        try {
            while (cursor.hasNext()) {
                documentCount +=1;
            }
        } finally {
            cursor.close();
        }

    }

}
