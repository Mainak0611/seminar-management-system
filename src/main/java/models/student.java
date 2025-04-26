package models;

public class student {
    private final String studentId;
    private final String name;
    private final String email;
    private final String password;
    private final String studentClass;
    private final String problemStatement;
    private final String facultyId; // ← New field

    public student(String studentId, String name, String email, String password, String studentClass, String problemStatement, String facultyId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentClass = studentClass;
        this.problemStatement = problemStatement;
        this.facultyId = facultyId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getProblemStatement() {
        return problemStatement;
    }

    public String getFacultyId() {
        return facultyId;
    }
}
