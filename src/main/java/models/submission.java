package models;

import org.bson.types.ObjectId;

public class submission {
    private final String teacherId;
    private final String studentId;
    private final String studentName;  // New field
    private final String submissionId;
    private final String type;
    private final ObjectId fileId;
    private int marks;

    // Updated constructor
    public submission(String teacherId, String studentId, String studentName, String submissionId, String type, ObjectId fileId, int marks) {
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.studentName = studentName;  // Initialize name
        this.submissionId = submissionId;
        this.type = type;
        this.fileId = fileId;
        this.marks = marks;
    }

    // Getter for studentName
    public String getStudentName() {
        return studentName;
    }

    // Other getters remain unchanged
    public int getMarks() {
        return marks;
    }

    public String getType() {
        return type;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public ObjectId getFileId() {
        return fileId;
    }


    public void setMarks(int marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Student Name: " + studentName + ", Student ID: " + studentId + ", Type: " + type + ", Marks: " + marks;
    }
}
