package models;

public class Topic {
    private String topic;
    private String domain;
    private String problemStatement;
    private String abstractText;

    public Topic(String topic, String domain, String problemStatement, String abstractText) {
        this.topic = topic;
        this.domain = domain;
        this.problemStatement = problemStatement;
        this.abstractText = abstractText;
    }

    public String getTopic() { return topic; }
    public String getDomain() { return domain; }
    public String getProblemStatement() { return problemStatement; }
    public String getAbstractText() { return abstractText; }
}
