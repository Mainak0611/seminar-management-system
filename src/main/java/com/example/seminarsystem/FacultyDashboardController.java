package com.example.seminarsystem;

import DOA.facultyDaoImp;
import DOA.notificationDaoImp;
import DOA.submissionDaoImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.faculty;
import models.notification;
import models.student;
import models.submission;
import com.example.seminarsystem.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDashboardController {

    private final facultyDaoImp facultyDao = new facultyDaoImp();
    private final submissionDaoImp submissionDao = new submissionDaoImp();
    private final notificationDaoImp notificationDao = new notificationDaoImp();

    private String facultyId;

    @FXML
    private ListView<String> studentSubmission, numOfStudents, noti;

    @FXML
    private MenuItem allocatedStudentsItem, viewSubmissionsItem, seeNotificationsItem, marksItem, sendNotificationItem;

    @FXML
    private TextArea name;

    // Static variable to remember the logged-in faculty ID
    public static String loggedInFacultyId;

    // Set faculty data including faculty ID
    public void setFacultyData(String facultyId) {
        this.facultyId = facultyId; // Set the faculty ID
        System.out.println("🔷 setFacultyData() called with: " + facultyId);

        faculty f = facultyDao.getFacultyById(facultyId);
        if (f != null) {
            String info = String.format(
                    "Name: %s%nEmail: %s%nDesignation: %s",
                    f.getName(),
                    f.getEmail(),
                    f.getDesignation()
            );
            name.setText(info); // Display faculty info

            // Store the current faculty in the session
            Session.setCurrentFaculty(f);
        } else {
            name.setText("⚠️ Could not load faculty info");
        }

        // Now load students for this faculty
        loadStudentSubmissions();
        loadTotalStudents();
        loadNotifications();
    }


    private void loadStudentSubmissions() {
        if (facultyId == null) return;

        List<submission> submissions = submissionDao.getSubmissionsToTeacher(facultyId);
        studentSubmission.getItems().clear();
        for (submission sub : submissions) {
            studentSubmission.getItems().add(sub.toString());
        }
    }

    private void loadTotalStudents() {
        if (facultyId == null) return;

        List<student> students = facultyDao.getStudentsByFacultyId(facultyId);
        numOfStudents.getItems().clear();
        numOfStudents.getItems().add("Total Students: " + students.size());

        for (student s : students) {
            numOfStudents.getItems().add(s.getStudentId() + " - " + s.getName());
        }
    }

    private void loadNotifications() {
        if (facultyId == null) return;

        List<notification> notifications = notificationDao.getNotificationsForCoordFac();
        List<String> coordinatorNotifs = new ArrayList<>();

        for (notification notif : notifications) {
            if ("Coordinator".equals(notif.getSentBy())) {
                coordinatorNotifs.add("From: " + notif.getSentBy() +
                        " - Notification: " + notif.getMessage());
            }
        }

        noti.getItems().setAll(
                coordinatorNotifs.isEmpty() ? List.of("No new coordinator notifications.") : coordinatorNotifs
        );
    }

    @FXML
    private void handleAllocatedStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("allocatedstudents.fxml"));
            Parent root = loader.load();

            // Pass facultyId to AllocatedStudentsController
            AllocatedStudentsController allocatedStudentsController = loader.getController();
            allocatedStudentsController.setFacultyId(facultyId); // Pass facultyId

            Stage stage = (Stage) studentSubmission.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Allocated Students");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewSubmissions() {
        navigateToPage("viewsubmissions.fxml", "View Submissions");
    }

    @FXML
    private void handleSeeNotifications() {
        navigateToPage("notifications.fxml", "Notifications");
    }

    public void rememberLoggedInFaculty(String facultyId) {
        this.facultyId = facultyId;
        loggedInFacultyId = facultyId; // Remember the faculty ID statically
        System.out.println("✅ Logged in Faculty ID remembered: " + facultyId);
    }

    @FXML
    private void handleMarks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("marks.fxml"));
            Parent root = loader.load();

            // Pass the facultyId to the MarksController
            MarksController marksController = loader.getController();
            marksController.setFacultyId(facultyId); // updated method

            Stage stage = (Stage) studentSubmission.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Marks");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendNotification() {
        navigateToPage("sendnotification.fxml", "Send Notification");
    }

    private void navigateToPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) studentSubmission.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}