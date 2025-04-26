package com.example.seminarsystem;

import DOA.StudentDaoImp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Topic;
import models.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopicSelectionController {

    private String studentId;
    private String studentName;

    private final StudentDaoImp studentDAO = new StudentDaoImp();

    // These fx:id fields must match your FXML
    @FXML private TextField topic1Field, domain1Field, problem1Field, abstract1Field;
    @FXML private TextField topic2Field, domain2Field, problem2Field, abstract2Field;
    @FXML private TextField topic3Field, domain3Field, problem3Field, abstract3Field;

    /**
     * Call this from the previous controller to pass in both studentId and studentName.
     */
    public void setStudentData(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        System.out.println("TopicSelectionController initialized with studentId="
                + studentId + ", studentName=" + studentName);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/seminarsystem/studentHomePage.fxml")
            );
            Parent root = loader.load();

            // Pass student data back to dashboard
            student student = studentDAO.getStudentById(studentId);
            if (student != null) {
                this.studentName = student.getName();
            }
            studentDashboardController controller = loader.getController();
            controller.initialize(studentId, studentName);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitTopics(ActionEvent event) {
        // 1) Gather all three topic entries
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic(
                topic1Field.getText(),
                domain1Field.getText(),
                problem1Field.getText(),
                abstract1Field.getText()
        ));
        topics.add(new Topic(
                topic2Field.getText(),
                domain2Field.getText(),
                problem2Field.getText(),
                abstract2Field.getText()
        ));
        topics.add(new Topic(
                topic3Field.getText(),
                domain3Field.getText(),
                problem3Field.getText(),
                abstract3Field.getText()
        ));

        // 2) Save to MongoDB with both studentId and studentName
        boolean success = studentDAO.saveStudentTopics(studentId, studentName, topics);

        // 3) Feedback
        if (success) {
            System.out.println("Topics submitted successfully for " + studentName);
        } else {
            System.err.println("Error saving topics for " + studentName);
        }
    }
}
