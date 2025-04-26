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
import models.*;
import org.bson.Document;

import javax.print.Doc;
import java.util.*;

public class adminDaoImp implements adminDAO {
    private final MongoCollection<Document> adminCollection;

    public adminDaoImp(MongoCollection<Document> adminCollection){
        this.adminCollection = MongoConnection.getCollection("admin");
    }

    @Override
    public void insertAdmin(admin admin) {
        Document doc = new Document("adminId", admin.getAdminId())
                .append("email", admin.getEmail())
                .append("password", admin.getPassword());

        adminCollection.insertOne(doc);
    }

    @Override
    public admin getAdminById(String adminId) {
        Document doc = adminCollection.find(Filters.eq("adminId", adminId)).first();
        return (doc != null) ? new admin(doc.getString("adminId"), doc.getString("email"), doc.getString("password")) : null;
    }

    @Override
    public admin getAdminByEmail(String email) {
        Document doc = adminCollection.find(Filters.eq("email", email)).first();
        return (doc != null) ? new admin(doc.getString("adminId"), doc.getString("email"), doc.getString("password")) : null;
    }

    @Override
    public List<admin> getAllAdmins() {
        List<admin> admins = new ArrayList<>();
        for (Document doc : adminCollection.find()) {
            admins.add(new admin(doc.getString("adminId"), doc.getString("email"), doc.getString("password")));
        }
        return admins;
    }

    @Override
    public void updateAdminPassword(String adminId, String newPassword) {
        adminCollection.updateOne(Filters.eq("adminId", adminId), Updates.set("password", newPassword));
    }

    @Override
    public void deleteAdmin(String adminId) {
        adminCollection.deleteOne(Filters.eq("adminId", adminId));
    }
}