    package com.example.seminarsystem;

    import DOA.facultyDaoImp;
    import DOA.StudentDaoImp;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.event.ActionEvent;
    import javafx.stage.Stage;
    import models.faculty;
    import models.student;
    import models.*;

    import java.io.IOException;
    import java.util.List;

    public class AllocatedStudentsController {
        private faculty faculty;

        @FXML
        private ListView<String> studentList;

        @FXML
        private TextField searchField, filterField;

        @FXML
        private Button backButton;

        private final ObservableList<String> allStudents = FXCollections.observableArrayList();

        private String facultyId;

        private facultyDaoImp facultyDao;
        private StudentDaoImp studentDao;

        // Method to set the Faculty data
        public void setFacultyData(faculty faculty) {
            this.faculty = faculty;
            // You can use this method to display or use faculty data in this controller.
            System.out.println("Faculty Data: " + faculty.getName() + " (" + faculty.getFacultyID() + ")");
        }


        public void setFacultyId(String facultyId) {
            this.facultyId = facultyId;
            facultyDao = new facultyDaoImp();  // Initialize the faculty DAO
            studentDao = new StudentDaoImp();  // Initialize the student DAO
            loadStudentsForFaculty();
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


        private void showAlert(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }
    }
