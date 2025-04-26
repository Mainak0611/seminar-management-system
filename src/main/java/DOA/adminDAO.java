package DOA;
import models.admin;
import java.util.*;

public interface adminDAO {
    void insertAdmin(admin admin);
    admin getAdminById(String adminId);
    admin getAdminByEmail(String email);
    List<admin> getAllAdmins();
    void updateAdminPassword(String adminId, String newPassword);
    void deleteAdmin(String adminId);
}
