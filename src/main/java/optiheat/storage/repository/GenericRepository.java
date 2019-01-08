package optiheat.storage.repository;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GenericRepository extends Neo4jRepository
{
    @Query("MATCH (n) DETACH DELETE n")
    void deleteAll();
}
