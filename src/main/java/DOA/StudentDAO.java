package DOA;

import models.Topic;
import models.student;
import java.util.List;

public interface StudentDAO {
    void insertStudent(student student);
    student getStudentById(String studentId);
    List<student> getAllStudents();
    void updateStudent(student student);
    void deleteStudent(String studentId);
    student getStudentByEmail(String email);
    boolean saveStudentTopics(String studentId, String studentName, List<Topic> topics);
}
