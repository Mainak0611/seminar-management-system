package DOA;

import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import models.Event;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class eventDaoImp implements EventDAO {
    private final MongoCollection<Document> eventsCollection;

    public eventDaoImp(){this.eventsCollection = MongoConnection.getCollection("events");}

    @Override
    public void addEvent(Event event) {
        Document doc = new Document("date", event.getDate().toString())
                .append("description", event.getDescription());

        eventsCollection.insertOne(doc);
    }

    @Override
    public List<Event> getUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        String today = LocalDate.now().toString();

        for (Document doc : eventsCollection.find(Filters.gte("date", today))) {
            LocalDate date = LocalDate.parse(doc.getString("date"));
            String description = doc.getString("description");

            Event e = new Event(date, description);
            events.add(e);
        }

        return events;
    }
}
