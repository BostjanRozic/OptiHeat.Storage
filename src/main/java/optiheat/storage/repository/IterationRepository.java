package optiheat.storage.repository;

import optiheat.storage.model.Iteration;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IterationRepository extends CrudRepository<Iteration, Long>
{
    Iteration findById(String id);

    @Query("MATCH (i:Iteration)--(:Unit {id:{unitId}}) RETURN i")
    List<Iteration> findIterationsForUnit(String unitId);
}
