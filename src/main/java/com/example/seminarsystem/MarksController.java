package com.example.seminarsystem;

import DOA.submissionDAO;
import DOA.submissionDaoImp;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.converter.IntegerStringConverter;
import models.faculty;
import models.submission;

import java.io.IOException;
import java.util.List;

public class MarksController {

    @FXML
    private TableView<submission> marksTableView;
    @FXML
    private TableColumn<submission, String> srNoCol;
    @FXML
    private TableColumn<submission, String> studentNameCol;
    @FXML
    private TableColumn<submission, String> typeCol;
    @FXML
    private TableColumn<submission, Integer> marksCol;

    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button backButton;

    private submissionDAO submissionDao = new submissionDaoImp(); // Using submissionDAO
    private List<submission> submissions;
    private String currentFacultyId;

    // Setter to inject faculty ID and load the submissions
    public void setFacultyId(String facultyId) {
        this.currentFacultyId = facultyId;
        loadSubmissions();
    }

    private void loadSubmissions() {
        // Fetch submissions for the given faculty (teacher)
        submissions = submissionDao.getSubmissionsToTeacher(currentFacultyId);
        ObservableList<submission> data = FXCollections.observableArrayList(submissions);

        srNoCol.setCellValueFactory(cellData -> {
            int index = marksTableView.getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyStringWrapper(String.valueOf(index));
        });

        studentNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStudentName()));
        typeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType()));

        // Editable marks column
        marksCol.setCellValueFactory(new PropertyValueFactory<>("marks"));
        marksCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        marksCol.setOnEditCommit(event -> {
            submission sub = event.getRowValue();
            int newMarks = event.getNewValue();
            System.out.println("Updating marks for submission " + sub.getSubmissionId() + " to " + newMarks);
            sub.setMarks(newMarks);  // Update the local object
        });

        marksTableView.setItems(data);
        marksTableView.setEditable(true);
    }

    @FXML
    private void handleConfirmChanges() {
        for (submission sub : marksTableView.getItems()) {
            // Confirm the update is being called with correct submissionId and marks
            System.out.println("Confirming update for submission " + sub.getSubmissionId() + " with marks " + sub.getMarks());
            submissionDao.updateMarks(sub.getSubmissionId(), sub.getMarks());  // Persist the updated marks in DB
        }
        showAlert("Marks Updated", "All marks have been updated successfully!");
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
