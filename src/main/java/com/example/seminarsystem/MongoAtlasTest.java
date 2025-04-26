package com.example.seminarsystem;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoAtlasTest {
    public static void main(String[] args) {
        // MongoDB Compass Default Connection String (Change if needed)
        String uri = "mongodb://localhost:27017";

        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            System.out.println("✅ Connected to MongoDB Compass successfully!");

            // Get a specific database (change as needed)
            MongoDatabase database = mongoClient.getDatabase("test");
            System.out.println("📂 Database: " + database.getName());

        } catch (Exception e) {
            System.err.println("❌ Connection Failed: " + e.getMessage());
        }
    }
}
