package com.example.seminarsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.Coordinator;

import java.io.IOException;

public class ProgressTrackingController {

    @FXML
    private Button backButton;

    private Coordinator coordinator;  // hold the Coordinator

    /** Called by the caller to pass in the coordinator. */
    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        // You can initialize view with coordinator info here if needed
        System.out.println("ProgressTrackingController got coordinator: "
                + coordinator.getCoordinatorId());
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
            Parent root = loader.load();

            // pass the coordinator back to the dashboard
            CoordinatorDashboardController dash = loader.getController();
            dash.setCoordinatorData(coordinator);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Coordinator Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
