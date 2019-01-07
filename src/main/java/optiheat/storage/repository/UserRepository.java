package optiheat.storage.repository;

import optiheat.storage.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findById(String id);

    @Query("MATCH (r:Room)-[:UNIT_ROOMS]-(:Unit)-[:USER_UNITS]-(:User {id:{userId}}) DETACH DELETE r")
    void deleteAllRoomsForUser(String userId);

    @Query("MATCH (u:Unit)-[:USER_UNITS]-(:User {id:{userId}}) DETACH DELETE u")
    void deleteAllUnitsForUser(String userId);

    @Query("MATCH (u:User {id:{userId}}) DELETE u")
    void deleteUser(String userId);
}
