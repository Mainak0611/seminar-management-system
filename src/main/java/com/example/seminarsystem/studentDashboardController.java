package com.example.seminarsystem;

import DOA.StudentDaoImp;
import DOA.notificationDaoImp;
import DOA.submissionDaoImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import models.notification;
import models.student;
import models.submission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class studentDashboardController {

    @FXML
    private MenuItem topicSelection;

    @FXML
    private TextArea infoDisplay;

    @FXML
    private ListView<String> submissionMarks;

    @FXML
    private ListView<String> noti;

    private final StudentDaoImp studentDao = new StudentDaoImp();
    private final submissionDaoImp submissionDao = new submissionDaoImp();
    private final notificationDaoImp notificationDao = new notificationDaoImp();


    private String studentId;
    private String studentName;

    // Updated initialization method to accept both studentId and studentName
    public void initialize(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName; // Store studentName for future use
        System.out.println("Initializing Dashboard with Student ID: " + studentId + " and Name: " + studentName);
        loadStudentDetails(studentId);
        loadSubmissions(studentId);
        loadNotifications(studentId);
    }

    private void loadStudentDetails(String studentId) {
        student student = studentDao.getStudentById(studentId);
        if (student != null) {
            String info = "Name: " + student.getName() + "\n" +
                    "PRN: " + student.getStudentId() + "\n" +
                    "Topic: " + student.getProblemStatement() + "\n" +
                    "Class: " + student.getStudentClass();
            infoDisplay.setText(info);
        } else {
            infoDisplay.setText("Student details not found!");
        }
    }

    private void loadSubmissions(String studentId) {
        List<submission> submissions = submissionDao.getSubmissionsByStudent(studentId);
        List<String> submissionDetails = new ArrayList<>();

        int count = 1;
        for (submission sub : submissions) {
            submissionDetails.add("Review " + count +
                    ", Marks: " + sub.getMarks());
            count++;
        }

        submissionMarks.getItems().setAll(
                submissionDetails.isEmpty() ? List.of("No submissions available.") : submissionDetails
        );
    }

    private void loadNotifications(String studentId) {
        List<notification> notifications = notificationDao.getNotificationsForUser(studentId);
        List<String> notificationMessages = new ArrayList<>();

        for (notification notif : notifications) {
            notificationMessages.add("From: " + notif.getSentBy() +
                    " - Notification ID: " + notif.getMessage());
        }

        noti.getItems().setAll(
                notificationMessages.isEmpty() ? List.of("No new notifications.") : notificationMessages
        );
    }

    @FXML
    private void switchToTopicSelection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/topicselection.fxml"));
            Parent root = loader.load();

            TopicSelectionController controller = loader.getController();
            controller.setStudentData(this.studentId, this.studentName); // Pass both
            // ✅ Fix: Use a known node (like your TextArea) to get the stage
            Stage stage = (Stage) infoDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToLMS(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/lms.fxml"));
            Parent root = loader.load();

            LMSController lmsController = loader.getController();
            lmsController.setStudentId(studentId);

            // ✅ Use a visible Node (like infoDisplay) to get the stage
            Stage stage = (Stage) infoDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToRaiseQuery(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/studentQuery.fxml"));
            Parent root = loader.load();

            StudentQueryController controller = loader.getController();
            controller.initialize(studentId, studentName); // Pass both studentId and studentName

            Stage stage = (Stage) infoDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
