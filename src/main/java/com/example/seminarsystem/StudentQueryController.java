package com.example.seminarsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Query;
import DOA.QueryDAO;
import DOA.QueryDaoImp;

import java.io.IOException;

public class StudentQueryController {

    private String studentId;
    private String studentName;

    @FXML
    private Label studentInfoLabel;

    @FXML
    private TextArea queryInput;

    @FXML
    private Button submitQueryButton;

    @FXML
    private Button backButton;

    // Updated initialize method to accept studentId and studentName
    public void initialize(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        System.out.println("StudentQueryController initialized with studentId: " + studentId + " and name: " + studentName);

        if (studentInfoLabel != null) {
            studentInfoLabel.setText("Logged in as: " + studentName + " (" + studentId + ")");
        }
    }

    @FXML
    private void handleSubmitQuery(ActionEvent event) {
        String queryText = queryInput.getText().trim();
        if (!queryText.isEmpty()) {
            try {
                // Updated: include studentName
                Query query = new Query(studentId, studentName, queryText);
                QueryDAO dao = new QueryDaoImp();
                dao.insertQuery(query);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Submission Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your query has been submitted to the system.");
                alert.showAndWait();

                queryInput.clear();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to submit query. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Query");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your issue before submitting.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/seminarsystem/studentHomePage.fxml"));
            Parent root = loader.load();

            studentDashboardController controller = loader.getController();
            controller.initialize(studentId, studentName); // Pass both studentId and studentName

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
