package com.example.seminarsystem;

import DOA.StudentDaoImp;
import DOA.facultyDaoImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import models.Coordinator;
import models.student;
import DOA.*;

import java.io.IOException;
import java.util.List;

public class CoordinatorAllocatedStudentsController {

    @FXML
    private ListView<String> studentList;

    @FXML
    private TextField searchField, filterField;

    @FXML
    private Button backButton;

    private Coordinator coordinator; // To hold the coordinator data

    private final ObservableList<String> allStudents = FXCollections.observableArrayList();

    private String facultyId;

    private facultyDaoImp facultyDao;
    private StudentDaoImp studentDao;

    // Method to set coordinator data from previous controller
    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;

        // For debugging purposes
        System.out.println("Loaded data for coordinator: " + coordinator.getCoordinatorId());

    }

    public void setFacultyId(String facultyId) {
        this.facultyId = coordinator.getCoordinatorId();
    }


    private void loadStudentsForFaculty() {
        // Fetch students for the given facultyId from MongoDB
        List<student> students = facultyDao.getStudentsByFacultyId(facultyId);

        allStudents.clear();
        for (student s : students) {
            String studentData = s.getStudentId() + " - " + s.getName() + " - " + s.getEmail() + " - " +
                    s.getStudentClass() + " - " + s.getProblemStatement();
            allStudents.add(studentData);
        }

        // Initially show all students
        studentList.setItems(FXCollections.observableArrayList(allStudents));
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();

        if (!keyword.isEmpty()) {
            ObservableList<String> filtered = allStudents.filtered(
                    s -> s.toLowerCase().contains(keyword)
            );
            studentList.setItems(filtered);
            if (filtered.isEmpty()) {
                showAlert("No results found for search: " + keyword);
            }
        } else {
            studentList.setItems(allStudents);
        }
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        String filter = filterField.getText().trim().toLowerCase();

        if (!filter.isEmpty()) {
            ObservableList<String> filtered = allStudents.filtered(
                    s -> s.toLowerCase().contains(filter)
            );
            studentList.setItems(filtered);
            if (filtered.isEmpty()) {
                showAlert("No results found for filter: " + filter);
            }
        } else {
            studentList.setItems(allStudents);
        }
    }
    @FXML
    private void handleBack() {
        try {
            // Load the previous scene (Coordinator Home Page)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
            Parent root = loader.load();

            // Pass the coordinator data back to the home page if necessary
            CoordinatorDashboardController controller = loader.getController();
            controller.setCoordinatorData(coordinator); // passing coordinator object

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Coordinator Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Action");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
