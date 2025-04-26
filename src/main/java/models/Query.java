package models;

public class Query {
    private String studentId;
    private String studentName;
    private String queryText;

    public Query(String studentId, String studentName, String queryText) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.queryText = queryText;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getQueryText() {
        return queryText;
    }
}
