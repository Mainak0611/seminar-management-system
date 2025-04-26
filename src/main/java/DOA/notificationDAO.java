package DOA;
import models.notification;
import java.util.*;

public interface notificationDAO {
    public void insertNotification(notification notification);
    notification getNotificationById(String notificationId);
    List<notification> getNotificationsForUser(String goTO);
    List<notification> getNotificationsSentBy(String sentBy);

}
