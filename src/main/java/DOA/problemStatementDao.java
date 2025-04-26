package DOA;
import models.problemStatement;
import java.util.*;

public interface problemStatementDao {
    void insertProblemStatement(problemStatement ps);
    problemStatement getProblemStatementById(String psId);
    List<problemStatement> getProblemStatementsByDomain(String domain);
    List<problemStatement> getAllProblemStatements();
}
