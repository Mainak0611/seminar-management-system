package DOA;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Query;
import org.bson.Document;

public class QueryDaoImp implements QueryDAO {

    private MongoCollection<Document> queryCollection;

    public QueryDaoImp() {
        MongoClient mongoClient = MongoClients.create();  // Connect to default MongoDB
        MongoDatabase database = mongoClient.getDatabase("seminarDatabase");
        queryCollection = database.getCollection("queries");
    }

    @Override
    public void insertQuery(Query query) {
        Document doc = new Document("studentId", query.getStudentId())
                .append("studentName", query.getStudentName())
                .append("queryText", query.getQueryText())
                .append("timestamp", System.currentTimeMillis());

        queryCollection.insertOne(doc);
    }

}
