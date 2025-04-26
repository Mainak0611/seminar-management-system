package DOA;

import models.submission;
import java.util.*;

public interface submissionDAO {
    void insertSubmission(submission submission);
    submission getSubmissionById(String submissionId);
    List<submission> getSubmissionsByStudent(String studentId);
    List<submission> getSubmissionsToTeacher(String teacherId);
    List<submission> getSubmissionsToCoordinator(String coordinatorId);
    submission getSubmissionType(String type);
    submission getSubmissionMarks(int marks);
    void updateMarks(String submissionId, int newMarks);
}
