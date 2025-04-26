package DOA;
import models.faculty;
import java.util.*;

public interface facultyDAO {
    faculty getFacultyById(String facultyId);
    List<faculty> getAllFaculty();
    void insertFaculty(faculty faculty);
    void updateFaculty(faculty faculty);
    void deleteFaculty(String facultyID);
    faculty getFacultyByEmail(String email);
    faculty getCoordinatorByEmail(String email);

}
