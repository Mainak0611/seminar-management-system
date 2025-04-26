package DOA;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Database.MongoConnection;
import models.Topic;
import models.student;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoImp implements StudentDAO {
    private final MongoCollection<Document> studentCollection;
    private final MongoDatabase database;

    public StudentDaoImp() {
        this.database = MongoConnection.getDatabase();
        this.studentCollection = database.getCollection("students");
    }

    @Override
    public student getStudentById(String studentId) {
        Document doc = studentCollection.find(Filters.eq("studentId", studentId)).first();
        if (doc != null) {
            return new student(
                    doc.getString("studentId"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("studentClass"),
                    doc.getString("problemStatement"),
                    doc.getString("facultyId") // <-- Added this line
            );
        }
        return null;
    }

    @Override
    public List<student> getAllStudents() {
        List<student> students = new ArrayList<>();
        for (Document doc : studentCollection.find()) {
            students.add(new student(
                    doc.getString("studentId"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("studentClass"),
                    doc.getString("problemStatement"),
                    doc.getString("facultyId") // <-- Added this line

            ));
        }
        return students;
    }

    @Override
    public void insertStudent(student student) {
        Document document = new Document("studentId", student.getStudentId())
                .append("name", student.getName())
                .append("email", student.getEmail())
                .append("password", student.getPassword())
                .append("studentClass", student.getStudentClass())
                .append("problemStatement", student.getProblemStatement());

        studentCollection.insertOne(document);
    }

    @Override
    public void updateStudent(student student) {
        studentCollection.updateOne(Filters.eq("studentId", student.getStudentId()),
                Updates.combine(
                        Updates.set("name", student.getName()),
                        Updates.set("email", student.getEmail()),
                        Updates.set("password", student.getPassword()),
                        Updates.set("studentClass", student.getStudentClass()),
                        Updates.set("problemStatement", student.getProblemStatement())
                ));
    }

    @Override
    public void deleteStudent(String studentId) {
        studentCollection.deleteOne(Filters.eq("studentId", studentId));
    }

    @Override
    public student getStudentByEmail(String email) {
        Document doc = studentCollection.find(Filters.eq("email", email)).first();

        if (doc != null) {
            return new student(
                    doc.getString("studentId"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("studentClass"),
                    doc.getString("problemStatement"),
                    doc.getString("facultyId") // <-- Added this line
            );
        }
        return null;
    }

    @Override
    public boolean saveStudentTopics(String studentId, String studentName, List<Topic> topics) {
        try {
            MongoCollection<Document> topicCollection = database.getCollection("topics");

            // Create a single document to hold all 3 topics
            Document doc = new Document("studentId", studentId)
                    .append("studentName", studentName);

            // Add topic1, domain1, etc.
            for (int i = 0; i < topics.size(); i++) {
                Topic t = topics.get(i);
                int index = i + 1;

                doc.append("topic" + index, t.getTopic());
                doc.append("domain" + index, t.getDomain());
                doc.append("problemStatement" + index, t.getProblemStatement());
                doc.append("abstract" + index, t.getAbstractText());
            }

            topicCollection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
