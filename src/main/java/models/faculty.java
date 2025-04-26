package models;
import java.util.*;

public class faculty {
    private String facultyID;
    private final String name;
    private final String email;
    private final String password;
    private final String designation;
    private List<student> students;


    public faculty(String facultyID, String name, String email, String password, String designation){
        this.facultyID = facultyID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.designation = designation;
        this.students = new ArrayList<>();
    }

    public String getFacultyID() {
        return facultyID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDesignation() {
        return designation;
    }

    public List<student> getStudents() {
        return students;
    }

    public void setStudents(List<student> students) {
        this.students = students;
    }

    public void addStudents(student student){
        this.students.add(student);
    }

    public void removeStudents(student student){
        this.students.remove(student);
    }

    public void setFacultyID(String facultyID){
        this.facultyID = facultyID;
    }


}

