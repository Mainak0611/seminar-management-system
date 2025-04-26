package com.example.seminarsystem;

import DOA.StudentDaoImp;
import DOA.eventDaoImp;
import DOA.EventDAO;
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
import models.Event;
import models.student;

import java.io.IOException;
import java.util.List;

public class studentLoginController {

    @FXML
    private Button button;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private final StudentDaoImp studentDAO = new StudentDaoImp();

    public void userLogin(ActionEvent event) throws IOException {
        student student = studentDAO.getStudentByEmail(email.getText());

        if (student != null && student.getPassword().trim().equals(password.getText().trim())) {
            // ✅ Show upcoming event notification
            showUpcomingEvents();

            // ✅ Load dashboard after alert
            switchScene(event, student.getStudentId(), student.getName());
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    public void switchScene(ActionEvent event, String studentId, String studentName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/studentHomePage.fxml"));
        Parent root = loader.load();

        studentDashboardController controller = loader.getController();
        controller.initialize(studentId, studentName);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ✅ New method to show events
    private void showUpcomingEvents() {
        EventDAO dao = new eventDaoImp();
        List<Event> events = dao.getUpcomingEvents();

        if (!events.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Event event : events) {
                builder.append("📅 Date: ").append(event.getDate())
                        .append("\n📝 Description: ").append(event.getDescription())
                        .append("\n\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Events");
            alert.setHeaderText("Heads up! Here's what's coming up:");
            alert.setContentText(builder.toString());
            alert.showAndWait();
        }
    }
}
