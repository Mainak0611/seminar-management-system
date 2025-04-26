package models;

public class admin {
    private final String adminId;
    private final String email;
    private final String password;

    public admin(String adminId, String email, String password){
        this.adminId = adminId;
        this.email = email;
        this.password = password;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
