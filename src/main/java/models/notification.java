package models;

public class notification {
    private final String notificationId;
    private String goTO;
    private final String sentBy;
    private String message;

    // Updated constructor to include message
    public notification(String notificationId, String goTO, String sentBy, String message) {
        this.notificationId = notificationId;
        this.goTO = goTO;
        this.sentBy = sentBy;
        this.message = message;
    }

    @Override
    public String toString() {
        return "From: " + sentBy + " - " + notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getGoTO() {
        return goTO;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
