package com.example.seminarsystem;


import DOA.submissionDaoImp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.submission;
import DOA.StudentDaoImp;
import models.student;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LMSController {

    private String studentId;
    private String studentName;

    private final StudentDaoImp studentDAO = new StudentDaoImp();

    // File paths to store uploaded files temporarily
    private String review1FilePath;
    private String review2FilePath;
    private String review3FilePath;

    @FXML
    private Button uploadReview1Btn, submitReview1Btn;
    @FXML
    private Button uploadReview2Btn, submitReview2Btn;
    @FXML
    private Button uploadReview3Btn, submitReview3Btn;
    @FXML
    private Label sampleLabel;

    public void setStudentId(String studentId) {
        this.studentId = studentId;
        student student = studentDAO.getStudentById(studentId);
        if (student != null) this.studentName = student.getName();
        System.out.println("LMSController: studentId=" + studentId + ", studentName=" + studentName);
    }

    @FXML
    private void initialize() {
        // Wire upload buttons
        uploadReview1Btn.setOnAction(e -> handleUpload(1));
        uploadReview2Btn.setOnAction(e -> handleUpload(2));
        uploadReview3Btn.setOnAction(e -> handleUpload(3));
        // Wire submit buttons
        submitReview1Btn.setOnAction(e -> handleSubmit(1));
        submitReview2Btn.setOnAction(e -> handleSubmit(2));
        submitReview3Btn.setOnAction(e -> handleSubmit(3));
    }

    private void handleUpload(int reviewNumber) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File for Review " + reviewNumber);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Allowed Files", "*.pdf", "*.jpg", "*.jpeg", "*.png")
        );

        File sel = chooser.showOpenDialog(null);
        if (sel != null) {
            String path = sel.getAbsolutePath().toLowerCase();
            if (path.endsWith(".pdf") || path.endsWith(".jpg") ||
                    path.endsWith(".jpeg") || path.endsWith(".png")) {

                switch (reviewNumber) {
                    case 1 -> review1FilePath = sel.getAbsolutePath();
                    case 2 -> review2FilePath = sel.getAbsolutePath();
                    case 3 -> review3FilePath = sel.getAbsolutePath();
                }
                showAlert("File Selected", sel.getName());
            } else {
                showAlert("Invalid Type", "Only PDF/JPG/PNG allowed");
            }
        } else {
            showAlert("No File", "Please select a file first");
        }
    }

    private void handleSubmit(int reviewNumber) {
        String filePath = switch (reviewNumber) {
            case 1 -> review1FilePath;
            case 2 -> review2FilePath;
            case 3 -> review3FilePath;
            default -> null;
        };

        if (filePath == null) {
            showAlert("No File", "Upload a file before submitting.");
            return;
        }

        try {
            // 1) Upload to GridFS
            submissionDaoImp dao = new submissionDaoImp();
            String fileName = new File(filePath).getName();
            ObjectId fileId = dao.uploadFileToGridFS(filePath, fileName);

            // 2) Create & save submission record
            String submissionId = UUID.randomUUID().toString();
            String type = "Review " + reviewNumber;
            String teacherId = "T000"; // <-- replace with actual teacherId as needed
            submission sub = new submission(teacherId, studentId, studentName, submissionId, type, fileId, 0);
            dao.insertSubmission(sub);

            showAlert("Success", type + " submitted!");
            System.out.println(type + " saved: fileId=" + fileId.toHexString());

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Upload Error", "Could not upload file: " + ex.getMessage());
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/studentHomePage.fxml"));
            Parent root = loader.load();

            // re-pass both studentId & studentName
            studentDashboardController ctrl = loader.getController();
            ctrl.initialize(studentId, studentName);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
