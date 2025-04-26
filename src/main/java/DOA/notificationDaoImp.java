package DOA;

import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import models.notification;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class notificationDaoImp implements notificationDAO {
    private final MongoCollection<Document> notificationCollection;

    public notificationDaoImp() {
        this.notificationCollection = MongoConnection.getCollection("notification");
    }

    @Override
    public void insertNotification(notification notification) {
        Document doc = new Document("notificationId", notification.getNotificationId())
                .append("goTO", notification.getGoTO())
                .append("sentBy", notification.getSentBy())
                .append("message", notification.getMessage());

        notificationCollection.insertOne(doc);
    }

    @Override
    public notification getNotificationById(String notificationId) {
        Document doc = notificationCollection.find(Filters.eq("notificationId", notificationId)).first();
        return (doc != null) ? new notification(
                doc.getString("notificationId"),
                doc.getString("goTO"),
                doc.getString("sentBy"),
                doc.getString("message")
        ) : null;
    }

    @Override
    public List<notification> getNotificationsForUser(String goTO) {
        List<notification> notifications = new ArrayList<>();
        for (Document doc : notificationCollection.find(Filters.eq("goTO", goTO))) {
            notifications.add(new notification(
                    doc.getString("notificationId"),
                    doc.getString("goTO"),
                    doc.getString("sentBy"),
                    doc.getString("message")
            ));
        }
        return notifications;
    }

    public List<notification> getNotificationsForCoordFac() {
        List<notification> notifications = new ArrayList<>();
        for (Document doc : notificationCollection.find(Filters.eq("sentBy", "Coordinator"))) {
            notifications.add(new notification(
                    doc.getString("notificationId"),
                    doc.getString("goTO"),
                    doc.getString("sentBy"),
                    doc.getString("message")
            ));
        }
        return notifications;
    }

    @Override
    public List<notification> getNotificationsSentBy(String sentBy) {
        List<notification> notifications = new ArrayList<>();
        for (Document doc : notificationCollection.find(Filters.eq("sentBy", sentBy))) {
            notifications.add(new notification(
                    doc.getString("notificationId"),
                    doc.getString("goTO"),
                    doc.getString("sentBy"),
                    doc.getString("message")
            ));
        }
        return notifications;
    }
}
