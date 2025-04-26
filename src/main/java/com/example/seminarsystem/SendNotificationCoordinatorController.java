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
import models.notification;
import org.bson.Document;
import models.*;

import java.io.IOException;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class SendNotificationCoordinatorController {
    private Coordinator coordinator;

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

    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        // You can use this method to display or use coordinator data in this controller.
        // For example, if you want to show coordinator name or ID:
        System.out.println("Coordinator Data: " + coordinator.getName() + " (" + coordinator.getCoordinatorId() + ")");
    }

    @FXML
    private void sendNotification() {
        String message = messageArea.getText().trim();

        if (!message.isEmpty()) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("seminarDatabase");

                MongoCollection<Document> studentCollection = database.getCollection("students");
                MongoCollection<Document> facultyCollection = database.getCollection("faculty");
                MongoCollection<Document> notifCollection = database.getCollection("notification");

                // Send to all students
                FindIterable<Document> students = studentCollection.find();
                for (Document student : students) {
                    String studentId = student.getString("studentId");
                    if (studentId != null) {
                        String notificationId = UUID.randomUUID().toString();
                        notification notif = new notification(notificationId, studentId, "Coordinator", message);

                        Document notifDoc = new Document("notificationId", notif.getNotificationId())
                                .append("goTO", notif.getGoTO())
                                .append("sentBy", notif.getSentBy())
                                .append("message", notif.getMessage());

                        notifCollection.insertOne(notifDoc);
                    }
                }

                // Send to all faculty
                FindIterable<Document> facultyList = facultyCollection.find();
                for (Document faculty : facultyList) {
                    String facultyId = faculty.getString("facultyId");
                    if (facultyId != null) {
                        String notificationId = UUID.randomUUID().toString();
                        notification notif = new notification(notificationId, facultyId, "Coordinator", message);

                        Document notifDoc = new Document("notificationId", notif.getNotificationId())
                                .append("goTO", notif.getGoTO())
                                .append("sentBy", notif.getSentBy())
                                .append("message", notif.getMessage());

                        notifCollection.insertOne(notifDoc);
                    }
                }

                messageArea.clear();
                System.out.println("Notifications sent to all students and faculty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






    @FXML private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
            Parent root = loader.load();

            // Get the CoordinatorDashboardController from the loaded FXML
            CoordinatorDashboardController ctrl = loader.getController();

            // Pass the coordinator data to the CoordinatorDashboardController
            ctrl.setCoordinatorData(coordinator);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Coordinator Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
