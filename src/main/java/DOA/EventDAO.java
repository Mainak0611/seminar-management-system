package DOA;


import models.Event;
import java.util.List;

public interface EventDAO {
    void addEvent(Event event);
    List<Event> getUpcomingEvents();
}