package DOA;

import Database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.faculty;
import models.student;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class facultyDaoImp implements facultyDAO {
    private final MongoCollection<Document> facultyCollection;
    private final MongoCollection<Document> studentCollection;

    public facultyDaoImp() {
        this.facultyCollection = MongoConnection.getCollection("faculty");
        this.studentCollection = MongoConnection.getCollection("students");
    }

    @Override
    public faculty getFacultyById(String facultyID) {
        Document document = facultyCollection.find(Filters.eq("facultyID", facultyID)).first();
        return document != null ? new faculty(
                document.getString("facultyID"),
                document.getString("name"),
                document.getString("email"),
                document.getString("password"),
                document.getString("designation")) : null;
    }

    @Override
    public List<faculty> getAllFaculty() {
        List<faculty> faculties = new ArrayList<>();
        for (Document document : facultyCollection.find()) {
            faculties.add(new faculty(
                    document.getString("facultyID"),
                    document.getString("name"),
                    document.getString("email"),
                    document.getString("password"),
                    document.getString("designation")));
        }
        return faculties;
    }

    @Override
    public void updateFaculty(faculty faculty) {
        facultyCollection.updateOne(Filters.eq("facultyID", faculty.getFacultyID()),
                Updates.combine(
                        Updates.set("name", faculty.getName()),
                        Updates.set("email", faculty.getEmail()),
                        Updates.set("password", faculty.getPassword()),
                        Updates.set("designation", faculty.getDesignation())
                ));
    }

    @Override
    public void insertFaculty(faculty faculty) {
        Document document = new Document("facultyID", faculty.getFacultyID())
                .append("name", faculty.getName())
                .append("email", faculty.getEmail())
                .append("password", faculty.getPassword())
                .append("designation", faculty.getDesignation());

        facultyCollection.insertOne(document);
    }

    @Override
    public void deleteFaculty(String facultyID) {
        facultyCollection.deleteOne(Filters.eq("facultyID", facultyID));
    }

    @Override
    public faculty getFacultyByEmail(String email) {
        Document doc = facultyCollection.find(Filters.eq("email", email)).first();
        if (doc != null) {
            return new faculty(
                    doc.getString("facultyID"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("designation")
            );
        }
        return null;
    }

    @Override
    public faculty getCoordinatorByEmail(String email) {
        // Get the coordinator by email from the faculty collection
        Document doc = facultyCollection.find(Filters.and(
                Filters.eq("email", email),
                Filters.eq("designation", "Coordinator") // Assuming the designation field indicates the type of faculty
        )).first();

        if (doc != null) {
            return new faculty(
                    doc.getString("facultyID"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("designation")
            );
        }
        return null;
    }

    public List<student> getStudentsByFacultyId(String facultyId) {
        List<student> students = new ArrayList<>();
        System.out.println("Fetching students for facultyId: " + facultyId);
        for (Document doc : studentCollection.find(Filters.eq("facultyId", facultyId))) {
            System.out.println("Student found: " + doc.toJson());
            students.add(new student(
                    doc.getString("studentId"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("studentClass"),
                    doc.getString("problemStatement"),
                    doc.getString("facultyId")
            ));
        }
        return students;
    }
}
