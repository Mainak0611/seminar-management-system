package com.example.seminarsystem;

import DOA.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;

public class facultyLoginController {

    @FXML
    private Button button;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private final facultyDaoImp facultyDao = new facultyDaoImp();
    private faculty loggedInFaculty;  // Store logged-in faculty

    @FXML
    public void initialize() {
        // This method will run when the scene loads
        System.out.println("Faculty Login Initialized");
    }

    public void userLogin(ActionEvent event) throws IOException {
        faculty faculty = facultyDao.getFacultyByEmail(email.getText().trim().toLowerCase());

        if (faculty != null) {
            System.out.println("Stored Password: '" + faculty.getPassword() + "'");
            System.out.println("Entered Password: '" + password.getText() + "'");

            if (faculty.getPassword().trim().equals(password.getText().trim())) {
                System.out.println("Login Successful!");
                loggedInFaculty = faculty;

                // Pass the facultyId to the FacultyDashboardController
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/facultyHomePage.fxml"));
                Parent root = loader.load();
                FacultyDashboardController controller = loader.getController();
                controller.setFacultyData(faculty.getFacultyID());  // Pass facultyId

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.out.println("Incorrect Password!");
            }
        } else {
            System.out.println("Email not found!");
        }
    }
}