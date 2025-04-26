package com.example.seminarsystem;

import DOA.CoordinatorDaoImp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Coordinator;

import java.io.IOException;

public class coordinatorLoginController {

    @FXML private Button button;
    @FXML private TextField email;
    @FXML private PasswordField password;

    private final CoordinatorDaoImp coordinatorDao = new CoordinatorDaoImp();

    // Login method
    public void coordinatorLogin(ActionEvent event) throws IOException {
        String enteredEmail = email.getText().trim().toLowerCase();
        String enteredPassword = password.getText().trim();

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            showAlert("Error", "Please fill in both email and password.");
            return;
        }

        // Fetch coordinator based on entered email
        Coordinator coordinator = coordinatorDao.getCoordinatorByEmail(enteredEmail);

        if (coordinator != null) {
            if (coordinator.getPassword().trim().equals(enteredPassword)) {
                // Login successful → load dashboard and pass the coordinator object
                switchToCoordinatorHome(event, coordinator);
                clearFields(); // Optionally clear the fields after successful login
            } else {
                showAlert("Incorrect Password", "The password you entered is incorrect.");
            }
        } else {
            showAlert("Email Not Found", "No coordinator found with the provided email.");
        }
    }

    // Method to switch to the coordinator home page
    public void switchToCoordinatorHome(ActionEvent event, Coordinator coordinator) throws IOException {
        // Load the FXML for the coordinator home page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
        Parent root = loader.load();

        // Get the controller and pass the coordinator ID
        CoordinatorDashboardController controller = loader.getController();
        controller.setCoordinatorData(coordinator);

        // Set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear the input fields
    private void clearFields() {
        email.clear();
        password.clear();
    }
}
