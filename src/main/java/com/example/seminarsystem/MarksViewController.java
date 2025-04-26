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
import models.Coordinator;
import models.submission;

import java.io.IOException;
import java.util.List;

public class MarksViewController {

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
    private String currentcoordinatorId;
    private Coordinator coordinator;

    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        // You can now use the coordinator object in your view, for example:
        System.out.println("Coordinator ID: " + coordinator.getCoordinatorId());


    }


    public void setFacultyId(String coordinatorId) {
        this.currentcoordinatorId = coordinatorId;
        loadSubmissions();
    }




    private void loadSubmissions() {
        String currentCoordinatorId = coordinator.getCoordinatorId();
        // Fetch submissions for the given coordinator
        submissions = submissionDao.getSubmissionsToCoordinator(currentCoordinatorId);
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
            System.out.println("Confirming update for submission " + sub.getSubmissionId() + " with marks " + sub.getMarks());
            submissionDao.updateMarks(sub.getSubmissionId(), sub.getMarks());
        }
        showAlert("Marks Updated", "All marks have been updated successfully!");
    }


    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}