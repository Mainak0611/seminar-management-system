package DOA;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.faculty;
import models.submission;
import models.student;
import models.notification;
import models.problemStatement;
import org.bson.Document;
import java.util.*;

public class problemStatementDaoImp implements problemStatementDao{
    private final MongoCollection<Document> problemStatementCollection;

    public problemStatementDaoImp(MongoCollection<Document> problemStatementCollection){
        this.problemStatementCollection = MongoConnection.getCollection("problemStatement");
    }
    @Override
    public void insertProblemStatement(problemStatement ps) {
        Document studentDoc = new Document("studentId", ps.getStudent().getStudentId())
                .append("name", ps.getStudent().getName())
                .append("email", ps.getStudent().getEmail())
                .append("studentClass", ps.getStudent().getStudentClass())
                .append("password", ps.getStudent().getPassword());

        Document doc = new Document("psId", ps.getPsId())
                .append("domain", ps.getDomain())
                .append("description", ps.getDescription())
                .append("statement", ps.getStatement())
                .append("student", studentDoc);

        problemStatementCollection.insertOne(doc);
    }

    @Override
    public problemStatement getProblemStatementById(String psId) {
        Document doc = problemStatementCollection.find(Filters.eq("psId", psId)).first();
        return documentToProblemStatement(doc);
    }

    @Override
    public List<problemStatement> getProblemStatementsByDomain(String domain) {
        List<problemStatement> statements = new ArrayList<>();
        for (Document doc : problemStatementCollection.find(Filters.eq("domain", domain))) {
            statements.add(documentToProblemStatement(doc));
        }
        return statements;
    }

    @Override
    public List<problemStatement> getAllProblemStatements() {
        List<problemStatement> statements = new ArrayList<>();
        for (Document doc : problemStatementCollection.find()) {
            statements.add(documentToProblemStatement(doc));
        }
        return statements;
    }

    private problemStatement documentToProblemStatement(Document doc) {
        if (doc == null) return null;

        Document studentDoc = (Document) doc.get("student");
        student studentObj = new student(
                studentDoc.getString("studentId"),
                studentDoc.getString("name"),
                studentDoc.getString("email"),
                studentDoc.getString("studentClass"),
                studentDoc.getString("problemStatement"),
                studentDoc.getString("password"),
                studentDoc.getString("facultyId") // <-- Added this line
        );

        return new problemStatement(
                doc.getString("psId"),
                doc.getString("domain"),
                doc.getString("description"),
                doc.getString("statement"),
                studentObj
        );
    }
}
