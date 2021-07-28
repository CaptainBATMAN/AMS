


// ! This file doesn't belong to the project. this file is for testing purposes only..






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
import com.mongodb.client.FindIterable;
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

public class playGround {
    // public static void main(String[] args) {

    // // ! params that are usually taken from request
    // String date = "11-11-2020";
    // String fromTimeShort = "12:00";
    // String toTimeShort = "13:30";

    // String fromTime = fromTimeShort+":00";
    // String toTime = toTimeShort+":00";

    // String subject = "USP";
    // String className = "CSE-06";
    // String period = "P2";

    // // ! converting Time strings to SimpleDateFormat Objects
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    // Date classStartTime = null;
    // try {
    // classStartTime = simpleDateFormat.parse(fromTime);
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // Date classEndTime = null;
    // try {
    // classEndTime = simpleDateFormat.parse(toTime);
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }

    // // ! to find total duration of class
    // int totalDurationOfClass = (int) ((classEndTime.getTime() -
    // classStartTime.getTime()) / 1000);

    // // ! to connect to the database and get required collection.
    // String collectionName = "db_" + date.replace("-", "_");
    // ConnectionString connectionString = new
    // ConnectionString("mongodb://127.0.0.1:27017");
    // MongoClient mongoClient = MongoClients.create(connectionString);
    // MongoDatabase database = mongoClient.getDatabase("university");
    // MongoCollection<org.bson.Document> collection =
    // database.getCollection(collectionName);

    // // ! to filter and get required data

    // Bson filter = eq("Meeting_ID", "ATCBCNNUPO");
    // Bson projection = Projections.fields(
    // Projections.include("Meeting_ID", "Participant_Email", "Duration",
    // "Start_Time", "End_Time"),
    // Projections.excludeId());

    // MongoCursor<org.bson.Document> cursor =
    // collection.find(filter).projection(projection).cursor();
    // org.bson.Document data = null;

    // long count = collection.countDocuments(filter);
    // System.out.println(count);
    // String[] studentEmailArray = new String[(int) count];
    // int totalDurationOfStudent = 0;
    // int durationOfStudent = 0;
    // int emailArrayIndex = 0;

    // try {

    // while (cursor.hasNext()) {
    // data = cursor.next();
    // String Participant_Email = data.getString("Participant_Email");
    // studentEmailArray[emailArrayIndex] = Participant_Email;
    // emailArrayIndex = emailArrayIndex + 1;
    // }

    // } finally {

    // // ! to remove duplicate emails from the emails array.
    // LinkedHashSet<String> lhSetColors = new
    // LinkedHashSet<String>(Arrays.asList(studentEmailArray));
    // String[] newStudentEmailArray = lhSetColors.toArray(new
    // String[lhSetColors.size()]);

    // // ! for each email we traverse through them...
    // for (String email : newStudentEmailArray) {

    // // ! filter to get all the entries for the given meeting Id and email
    // Bson studentFilter = and(eq("Meeting_ID", "ATCBCNNUPO"),
    // eq("Participant_Email", email),
    // eq("PeriodWiseModified", null));
    // Bson studentProjection = Projections.fields(
    // Projections.include("Meeting_ID", "Participant_Email", "Duration",
    // "Start_Time", "End_Time"),
    // Projections.excludeId());

    // MongoCursor<org.bson.Document> studentCursor = collection.find(studentFilter)
    // .projection(studentProjection).cursor();

    // try {
    // totalDurationOfStudent = 0;

    // while (studentCursor.hasNext()) {

    // org.bson.Document studentData = studentCursor.next();

    // Date studentStartTime =
    // simpleDateFormat.parse(studentData.getString("Start_Time"));
    // Date studentEndTime =
    // simpleDateFormat.parse(studentData.getString("End_Time"));

    // // ! Case-1 if student was already logged in before the class started..
    // if ((studentStartTime.getTime() <= classStartTime.getTime())
    // && ((studentEndTime.getTime() >= classStartTime.getTime()
    // && studentEndTime.getTime() <= classEndTime.getTime()))) {
    // durationOfStudent = (int) ((studentEndTime.getTime() -
    // classStartTime.getTime()) / 1000);
    // totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
    // }

    // // ! Case-2 if the student stayed even after the class Ended..

    // if ((studentEndTime.getTime() >= classEndTime.getTime())
    // && ((studentStartTime.getTime() >= classStartTime.getTime())
    // && studentStartTime.getTime() <= classEndTime.getTime())) {
    // durationOfStudent = (int) ((classEndTime.getTime() -
    // studentStartTime.getTime()) / 1000);
    // totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
    // }

    // // ! Case-3 Student joined in time and exited in time.
    // if ((studentStartTime.getTime() >= classStartTime.getTime()
    // && studentStartTime.getTime() <= classEndTime.getTime())
    // && (studentEndTime.getTime() >= classStartTime.getTime()
    // && studentEndTime.getTime() <= classEndTime.getTime())) {
    // durationOfStudent = studentData.getInteger("Duration");
    // totalDurationOfStudent = totalDurationOfStudent + durationOfStudent;
    // }
    // }
    // } catch (ParseException e) {
    // e.printStackTrace();
    // } finally {

    // String Meeting_ID = data.getString("Meeting_ID");
    // // ! this will be helpful if the student is logged in from two devices at the
    // // ! same time or he used screen share to present his screen.
    // if (totalDurationOfStudent > totalDurationOfClass) {
    // totalDurationOfStudent = totalDurationOfClass;
    // }

    // System.out.println(totalDurationOfClass);
    // System.out.println(totalDurationOfStudent);

    // // ! Period object, the important part..
    // JSONObject subJsonObject = new JSONObject();
    // subJsonObject.put("Meeting_ID", Meeting_ID);
    // subJsonObject.put("Class", className);
    // String classTimings = fromTimeShort + " to " + toTimeShort;
    // subJsonObject.put("Class_Timings", classTimings);
    // subJsonObject.put("Subject", subject);
    // subJsonObject.put("Duration", totalDurationOfStudent);

    // // ! To check if the PeriodWiseModified document exist in the DB..
    // Bson pwmFilter = and(eq("Meeting_ID", "ATCBCNNUPO"), eq("Participant_Email",
    // email),
    // eq("PeriodWiseModified", true));
    // long pwmCount = collection.countDocuments(pwmFilter);
    // if (pwmCount == 1) {
    // System.out.println("Upsert update the data");

    // Bson periodUpdate = Updates.set(period, subJsonObject);
    // UpdateOptions options = new UpdateOptions().upsert(true);
    // collection.updateOne(pwmFilter, periodUpdate, options);

    // } else {
    // System.out.println("no document created");
    // Document document = new Document("Participant_Email",email)
    // .append("Meeting_ID", Meeting_ID)
    // .append("PeriodWiseModified", true)
    // .append(period,subJsonObject);
    // collection.insertOne(document);
    // }

    // studentCursor.close();
    // }

    // }
    // cursor.close();
    // }
    // }

    public static void main(String[] args) {

        // * get params from request
        String date = "11-11-2020";
        String subject = "USP";
        String className = "CSE-05";
        String meetingID = "";


        // * creating DB instance
        String collectionName = "db_" + date.replace("-", "_");
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("university");
        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

        // * filtering data and fields needed.
        Bson newFilter;
        newFilter = and(eq("PeriodWiseModified", true));
        Bson projection = Projections.fields(Projections.include("Participant_Email", "Meeting_ID", "P1", "P2", "P3"),
                Projections.excludeId());

        long count = collection.countDocuments(newFilter);
        System.out.println(count);
        MongoCursor<org.bson.Document> cursor = collection.find(newFilter).projection(projection).cursor();
        org.bson.Document data = null;  
        JSONArray array = new JSONArray();

        try {

            // * traversing through all the documents
            while (cursor.hasNext()) {
                data = cursor.next();
                JSONObject jsonObject = new JSONObject();

                Bson p1Filter = and(eq("P1.Subject",subject),eq("P1.Class",className));
                Bson p2Filter = and(eq("P2.Subject",subject),eq("P2.Class",className));
                Bson p3Filter = and(eq("P3.Subject",subject),eq("P3.Class",className));

                long p1Count,p2Count,p3Count ;
                p1Count = collection.countDocuments(p1Filter);
                p2Count = collection.countDocuments(p2Filter);
                p3Count = collection.countDocuments(p3Filter);
                System.out.println(p1Count);
                System.out.println(p2Count);
                System.out.println(p3Count);

                if (p1Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P1",Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P1",Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P1",Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P1",Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
                if (p2Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P2",Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P2",Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P2",Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P2",Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
                if (p3Count > 0) {
                    jsonObject.put("Participant_Email", data.getString("Participant_Email"));
                    jsonObject.put("Meeting_ID", data.get("P3",Document.class).getString("Meeting_ID"));
                    jsonObject.put("Class", data.get("P3",Document.class).getString("Class"));
                    jsonObject.put("Subject", data.get("P3",Document.class).getString("Subject"));
                    jsonObject.put("Duration", data.get("P3",Document.class).getInteger("Duration"));
                    array.add(jsonObject);
                }
            }
        } finally {
            // * Sending JSOM array as a response
            System.out.println(array);
            cursor.close();
        }
    }
}
