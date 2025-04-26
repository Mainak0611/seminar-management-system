package com.example.seminarsystem;

import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Coordinator;
import org.bson.Document;
import com.mongodb.client.MongoCursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyStudentManagerController {

    @FXML private ListView<String> facultyListView;
    @FXML private Button backButton;

    private Coordinator coordinator;

    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        loadFacultyAndStudents();
    }

    private void loadFacultyAndStudents() {
        facultyListView.getItems().clear();

        MongoCollection<Document> facultyCol  = MongoConnection.getCollection("faculty");
        MongoCollection<Document> studentCol  = MongoConnection.getCollection("students");  // ← fixed!

        // fetch faculties under this coordinator
        List<Document> faculties = facultyCol
                .find(Filters.eq("coordinatorID", coordinator.getCoordinatorId()))
                .into(new ArrayList<>());

        System.out.println("Found " + faculties.size() + " faculties for coordinator " + coordinator.getCoordinatorId());

        if (faculties.isEmpty()) {
            facultyListView.getItems().add("❌ No faculties assigned to this coordinator.");
            return;
        }

        for (Document fdoc : faculties) {
            String fname = fdoc.getString("name");
            String fid   = fdoc.getString("facultyID");

            facultyListView.getItems().add("👨‍🏫 " + fname + " (" + fid + ")");

            // now fetch students for this faculty
            List<Document> studs = studentCol
                    .find(Filters.eq("facultyId", fid))
                    .into(new ArrayList<>());

            System.out.println("  → faculty " + fid + " has " + studs.size() + " students");

            if (studs.isEmpty()) {
                facultyListView.getItems().add("    ⚠ No students assigned.");
            } else {
                for (Document sdoc : studs) {
                    String sname = sdoc.getString("name");
                    String sid   = sdoc.getString("studentId");
                    facultyListView.getItems().add("    • " + sname + " (" + sid + ")");
                }
            }

            facultyListView.getItems().add(""); // spacing
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
            Parent root = loader.load();

            CoordinatorDashboardController ctrl = loader.getController();
            ctrl.setCoordinatorData(coordinator);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Coordinator Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
