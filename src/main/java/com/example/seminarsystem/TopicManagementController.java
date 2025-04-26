package com.example.seminarsystem;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Coordinator;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;

public class TopicManagementController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<TopicEntry> topicTable;
    @FXML
    private TableColumn<TopicEntry, String> studentIdCol;
    @FXML
    private TableColumn<TopicEntry, String> studentNameCol;
    @FXML
    private TableColumn<TopicEntry, String> topicCol;
    @FXML
    private TableColumn<TopicEntry, String> domainCol;
    @FXML
    private TableColumn<TopicEntry, String> problemCol;
    @FXML
    private TableColumn<TopicEntry, String> abstractCol;
    @FXML
    private TableColumn<TopicEntry, Void> approvalCol;

    private Coordinator coordinator;

    public void setCoordinatorData(Coordinator coordinator) {
        this.coordinator = coordinator;
        System.out.println("Coordinator ID: " + coordinator.getCoordinatorId());
        loadTopics();
    }

    private void loadTopics() {
        ObservableList<TopicEntry> topicList = FXCollections.observableArrayList();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("seminarDatabase");
            MongoCollection<Document> topics = db.getCollection("topics");

            for (Document doc : topics.find()) {
                String studentId = doc.getString("studentId");
                String studentName = doc.getString("studentName");
                ObjectId docId = doc.getObjectId("_id");

                for (int i = 1; i <= 3; i++) {
                    String topic = doc.getString("topic" + i);
                    String domain = doc.getString("domain" + i);
                    String problem = doc.getString("problemStatement" + i);
                    String abs = doc.getString("abstract" + i);
                    Boolean approved = doc.getBoolean("approved" + i, false);

                    if (topic != null && !topic.isEmpty()) {
                        topicList.add(new TopicEntry(studentId, studentName, topic, domain, problem, abs, docId, i, approved));
                    }
                }
            }
        }

        topicTable.setItems(topicList);

        studentIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().studentId));
        studentNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().studentName));
        topicCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().topic));
        domainCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().domain));
        problemCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().problemStatement));
        abstractCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().abstractText));

        approvalCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");

            {
                approveBtn.setOnAction(e -> {
                    TopicEntry entry = getTableView().getItems().get(getIndex());
                    approveTopic(entry.documentId, entry.topicNumber);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TopicEntry entry = getTableView().getItems().get(getIndex());
                    if (entry.approved != null && entry.approved) {
                        approveBtn.setText("Approved");
                        approveBtn.setDisable(true);
                    } else {
                        approveBtn.setText("Approve");
                        approveBtn.setDisable(false);
                    }
                    setGraphic(approveBtn);
                }
            }
        });
    }

    private void approveTopic(ObjectId id, int topicNumber) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("seminarDatabase");
            MongoCollection<Document> topics = db.getCollection("topics");

            topics.updateOne(new Document("_id", id),
                    new Document("$set", new Document("approved" + topicNumber, true)));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Topic " + topicNumber + " Approved!", ButtonType.OK);
            alert.showAndWait();

            loadTopics(); // Refresh table
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/seminarsystem/coordinatorHomePage.fxml"));
            Parent root = loader.load();

            CoordinatorDashboardController controller = loader.getController();
            controller.setCoordinatorData(coordinator);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Faculty Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Internal helper class to represent a topic row
    private static class TopicEntry {
        String studentId;
        String studentName;
        String topic;
        String domain;
        String problemStatement;
        String abstractText;
        ObjectId documentId;
        int topicNumber;
        Boolean approved;

        TopicEntry(String studentId, String studentName, String topic, String domain,
                   String problemStatement, String abstractText, ObjectId documentId, int topicNumber, Boolean approved) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.topic = topic;
            this.domain = domain;
            this.problemStatement = problemStatement;
            this.abstractText = abstractText;
            this.documentId = documentId;
            this.topicNumber = topicNumber;
            this.approved = approved;
        }
    }
}
