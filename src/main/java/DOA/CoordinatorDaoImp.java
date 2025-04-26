package DOA;

import models.Coordinator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import Database.MongoConnection;


import java.util.ArrayList;
import java.util.List;

public class CoordinatorDaoImp implements coordinatorDAO {

    // Assuming you have a MongoDB connection setup
    private MongoCollection<Document> collection;

    public CoordinatorDaoImp() {
        // Initialize your MongoDB connection
        // Assuming you're using MongoDB with a collection for coordinators
        collection = MongoConnection.getDatabase().getCollection("coordinator");
    }

    @Override
    public Coordinator getCoordinatorByEmail(String email) {
        // Find the coordinator by email
        Document query = new Document("email", email);
        Document result = collection.find(query).first();

        if (result != null) {
            return mapDocumentToCoordinator(result);
        } else {
            return null;
        }
    }

    @Override
    public Coordinator getCoordinatorById(String coordinatorId) {
        // Find the coordinator by ID
        Document query = new Document("coordinatorId", coordinatorId);
        Document result = collection.find(query).first();

        if (result != null) {
            return mapDocumentToCoordinator(result);
        } else {
            return null;
        }
    }

    @Override
    public List<Coordinator> getAllCoordinators() {
        // Fetch all coordinators
        List<Coordinator> coordinators = new ArrayList<>();
        FindIterable<Document> results = collection.find();

        for (Document document : results) {
            coordinators.add(mapDocumentToCoordinator(document));
        }
        return coordinators;
    }

    // Helper method to convert MongoDB document to coordinator object
    private Coordinator mapDocumentToCoordinator(Document document) {
        Coordinator c = new Coordinator();
        c.setCoordinatorId(document.getString("coordinatorId"));
        c.setName(document.getString("name"));
        c.setEmail(document.getString("email"));
        c.setPassword(document.getString("password"));
        c.setDesignation(document.getString("designation"));
        return c;
    }
}
