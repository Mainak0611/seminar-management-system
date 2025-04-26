package models;

public class problemStatement {
    private final String psId;
    private final String domain;
    private final String description;
    private final String statement;
    private final student student;

    public problemStatement(String psId,String domain, String description, String statement, student student){
        this.psId = psId;
        this.domain = domain;
        this.description = description;
        this.statement = statement;
        this.student = student;
    }

    public String getPsId() {
        return psId;
    }

    public String getDomain() {
        return domain;
    }

    public String getDescription() {
        return description;
    }

    public String getStatement() {
        return statement;
    }

    public student getStudent() {
        return student;
    }
}
