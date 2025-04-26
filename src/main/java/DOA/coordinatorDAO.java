package DOA;

import models.Coordinator;

import java.util.List;

public interface coordinatorDAO {

    Coordinator getCoordinatorByEmail(String email);

    Coordinator getCoordinatorById(String coordinatorId);

    List<Coordinator> getAllCoordinators();
}
