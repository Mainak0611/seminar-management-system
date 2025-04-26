package com.example.seminarsystem;

import DOA.EventDAO;
import DOA.eventDaoImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Coordinator;
import models.Event;

import java.io.IOException;
import java.time.LocalDate;

public class EventScheduleController {

    @FXML private DatePicker datePicker;
    @FXML private TextField descriptionField;
    @FXML private Button postButton;
    @FXML private Button backButton;

    private Coordinator coordinator; // Declare the Coordinator object

    // Method to set the Coordinator data
    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        // You can use this method to display or use coordinator data in this controller.
        // For example, if you want to show coordinator name or ID:
        System.out.println("Coordinator Data: " + coordinator.getName() + " (" + coordinator.getCoordinatorId() + ")");
    }

    @FXML private void initialize() {
        postButton.setOnAction(e -> handlePost());
    }

    private void handlePost() {
        LocalDate selectedDate = datePicker.getValue();
        String description = descriptionField.getText();

        if (selectedDate == null || description.isEmpty()) {
            showAlert(AlertType.ERROR, "Please fill in both the date and the description.");
            return;
        }

        Event event = new Event(selectedDate, description);
        EventDAO dao = new eventDaoImp();
        dao.addEvent(event);

        showAlert(AlertType.INFORMATION, "Meeting scheduled on " + selectedDate + " with description:\n" + description);

        datePicker.setValue(null);
        descriptionField.clear();
    }


    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Schedule Event");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
