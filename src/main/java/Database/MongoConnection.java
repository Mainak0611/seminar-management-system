package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {
    private static final String connectionHost = "mongodb://localhost:27017";
    private static final String dbName = "seminarDatabase";

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    static {
        try {
            mongoClient = MongoClients.create(connectionHost);
            mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Couldn't connect: " + e.getMessage());
        }
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }

    // ✅ ADD THIS METHOD:
    public static MongoDatabase getDatabase() {
        return mongoDatabase;
    }
}
