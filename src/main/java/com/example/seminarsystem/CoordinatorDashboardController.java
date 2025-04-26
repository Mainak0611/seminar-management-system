package com.example.seminarsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Coordinator;

import java.io.IOException;

public class CoordinatorDashboardController {

    @FXML private MenuItem studentFacultyItem;
    @FXML private MenuItem marksItem;
    @FXML private MenuItem topicManagementItem;
    @FXML private MenuItem eventScheduleItem;
    @FXML private MenuItem sendNotificationItem;
    @FXML private MenuItem progressTrackingItem;
    @FXML private MenuItem allocatedStudentsItem;

    @FXML private AnchorPane rootPane;
    @FXML private TextArea coordinatorInfoArea;

    private Coordinator coordinator;

    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;

        System.out.println("Coordinator ID: " + coordinator.getCoordinatorId());

        String info = String.format(
                "Name: %s\nEmail: %s\nID: %s",
                coordinator.getName(),
                coordinator.getEmail(),
                coordinator.getCoordinatorId()
        );
        coordinatorInfoArea.setText(info);
    }



    // Navigation for Student-Faculty Mapping
    @FXML private void handleStudentFaculty() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/facultyStudentManager.fxml"));
        Parent root = loader.load();

        FacultyStudentManagerController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void handleMarksView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/marksView.fxml"));
        Parent root = loader.load();

        // Ensure you are getting the correct controller instance
        MarksViewController controller = loader.getController();

        // Pass the coordinator data to the MarksViewController
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigation for Topic Management
    @FXML private void handleTopicManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/topicManagement.fxml"));
        Parent root = loader.load();

        TopicManagementController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigation for Event Scheduling
    @FXML private void handleEventScheduling() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/eventSchedule.fxml"));
        Parent root = loader.load();

        EventScheduleController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigation for Sending Notifications
    @FXML private void handleSendNotifications() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorNotification.fxml"));
        Parent root = loader.load();
        SendNotificationCoordinatorController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigation for Progress Tracking
    @FXML private void handleProgressTracking() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/progressTracking.fxml"));
        Parent root = loader.load();

        ProgressTrackingController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigation for Allocated Students
    @FXML private void handleAllocatedStudents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorAllocatedStudents.fxml"));
        Parent root = loader.load();

        CoordinatorAllocatedStudentsController controller = loader.getController();
        controller.setCoordinatorData(coordinator); // Pass coordinator for filtering

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
