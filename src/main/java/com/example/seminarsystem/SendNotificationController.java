package com.example.seminarsystem;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.faculty;
import models.notification;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class SendNotificationController {

    @FXML
    private TextField recipientIdField;

    @FXML
    private TextArea messageArea;

    @FXML
    private Button sendButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        sendButton.setOnAction(e -> sendNotification());
    }

    @FXML
    private void sendNotification() {
        String message = messageArea.getText().trim();

        if (!message.isEmpty()) {
            // Replace with actual logged-in faculty ID (can be passed via a method)
            String facultyId = "F001"; // Example hardcoded ID

            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("seminarDatabase");
                MongoCollection<Document> studentCollection = database.getCollection("students");

                // Query all students with the matching facultyId
                FindIterable<Document> students = studentCollection.find(eq("facultyId", facultyId));

                MongoCollection<Document> notifCollection = database.getCollection("notification");

                for (Document student : students) {
                    String studentId = student.getString("studentId");

                    if (studentId != null) {
                        String notificationId = UUID.randomUUID().toString();
                        notification notif = new notification(notificationId, studentId, "Faculty", message);

                        Document notifDoc = new Document("notificationId", notif.getNotificationId())
                                .append("goTO", notif.getGoTO())
                                .append("sentBy", notif.getSentBy())
                                .append("message", notif.getMessage());

                        notifCollection.insertOne(notifDoc);
                    }
                }

                messageArea.clear();
            }
        }
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/facultyHomePage.fxml"));
            Parent root = loader.load();

            // Retrieve the faculty from the session
            FacultyDashboardController facultyDashboardController = loader.getController();
            faculty facultyFromSession = Session.getCurrentFaculty();
            if (facultyFromSession != null) {
                facultyDashboardController.setFacultyData(facultyFromSession.getFacultyID());  // Set the facultyId
            } else {
                // Handle case where session faculty is null
                System.out.println("No faculty in session.");
            }

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Faculty Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}