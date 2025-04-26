package DOA;

import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.submission;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class submissionDaoImp implements submissionDAO {
    private final MongoCollection<Document> submissionCollection;
    private final GridFSBucket gridFSBucket;

    public submissionDaoImp() {
        // Initialize MongoDB collections
        this.submissionCollection = MongoConnection.getCollection("submission");
        this.gridFSBucket = GridFSBuckets.create(MongoConnection.getDatabase(), "submissionFiles");
    }

    @Override
    public void updateMarks(String submissionId, int newMarks) {
        // Make sure submissionId is a String and matches exactly in the database
        submissionCollection.updateOne(
                Filters.eq("submissionId", submissionId),  // Filter by submissionId
                Updates.set("marks", newMarks)  // Set the new marks value
        );
        System.out.println("Updated marks for submissionId: " + submissionId + " to " + newMarks);
    }

    public ObjectId uploadFileToGridFS(String filePath, String fileName) throws IOException {
        try (FileInputStream streamToUploadFrom = new FileInputStream(filePath)) {
            GridFSUploadOptions options = new GridFSUploadOptions().chunkSizeBytes(358400);
            return gridFSBucket.uploadFromStream(fileName, streamToUploadFrom, options);
        }
    }

    @Override
    public void insertSubmission(submission submission) {
        // Convert submission to MongoDB document and insert
        Document doc = new Document("submissionId", submission.getSubmissionId())
                .append("studentId", submission.getStudentId())
                .append("studentName", submission.getStudentName())  // Store student name
                .append("teacherId", submission.getTeacherId())
                .append("type", submission.getType())
                .append("marks", submission.getMarks())
                .append("fileId", submission.getFileId().toHexString());

        submissionCollection.insertOne(doc);
    }

    @Override
    public submission getSubmissionById(String submissionId) {
        Document doc = submissionCollection.find(Filters.eq("submissionId", submissionId)).first();
        return (doc != null) ? new submission(
                doc.getString("teacherId"),
                doc.getString("studentId"),
                doc.getString("studentName"),
                doc.getString("submissionId"),
                doc.getString("type"),
                new ObjectId(doc.getString("fileId")),
                doc.getInteger("marks")
        ) : null;
    }

    @Override
    public List<submission> getSubmissionsByStudent(String studentId) {
        List<submission> submissions = new ArrayList<>();
        for (Document doc : submissionCollection.find(Filters.eq("studentId", studentId))) {
            submissions.add(new submission(
                    doc.getString("teacherId"),
                    doc.getString("studentId"),
                    doc.getString("studentName"),
                    doc.getString("submissionId"),
                    doc.getString("type"),
                    new ObjectId(doc.getString("fileId")),
                    doc.getInteger("marks")
            ));
        }
        return submissions;
    }

    @Override
    public List<submission> getSubmissionsToTeacher(String teacherId) {
        List<submission> submissions = new ArrayList<>();
        for (Document doc : submissionCollection.find(Filters.eq("teacherId", teacherId))) {
            submissions.add(new submission(
                    doc.getString("teacherId"),
                    doc.getString("studentId"),
                    doc.getString("studentName"),
                    doc.getString("submissionId"),
                    doc.getString("type"),
                    new ObjectId(doc.getString("fileId")),
                    doc.getInteger("marks")
            ));
        }
        return submissions;
    }

    @Override
    public List<submission> getSubmissionsToCoordinator(String coordinatorId) {
        List<submission> submissions = new ArrayList<>();
        for (Document doc : submissionCollection.find(Filters.eq("coordinatorId", coordinatorId))) {
            submissions.add(new submission(
                    doc.getString("coordinatorId"),
                    doc.getString("studentId"),
                    doc.getString("studentName"),
                    doc.getString("submissionId"),
                    doc.getString("type"),
                    new ObjectId(doc.getString("fileId")),
                    doc.getInteger("marks")
            ));
        }
        return submissions;
    }


    @Override
    public submission getSubmissionType(String type) {
        Document doc = submissionCollection.find(Filters.eq("type", type)).first();
        return (doc != null) ? new submission(
                doc.getString("teacherId"),
                doc.getString("studentId"),
                doc.getString("studentName"),
                doc.getString("submissionId"),
                doc.getString("type"),
                new ObjectId(doc.getString("fileId")),
                doc.getInteger("marks")
        ) : null;
    }

    @Override
    public submission getSubmissionMarks(int marks) {
        Document doc = submissionCollection.find(Filters.eq("marks", marks)).first();
        return (doc != null) ? new submission(
                doc.getString("teacherId"),
                doc.getString("studentId"),
                doc.getString("studentName"),
                doc.getString("submissionId"),
                doc.getString("type"),
                new ObjectId(doc.getString("fileId")),
                doc.getInteger("marks")
        ) : null;
    }
}
