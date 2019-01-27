package optiheat.storage.repository;

import optiheat.storage.model.Iteration;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IterationRepository extends CrudRepository<Iteration, Long>
{
    Iteration findById(String id);

    //@Query("MATCH (i:Iteration)--(:Unit {id:{unitId}}) RETURN i")
    //List<Iteration> findIterationsForUnit(String unitId);

    @Query("MATCH (i:Iteration)--(:Unit {id:{unitId}}) OPTIONAL MATCH (i:Iteration)-[irs:ITERATION_ROOMSETTINGS]-(rs:RoomSetting) OPTIONAL MATCH (i:Iteration)-[irm:ITERATION_ROOMMEASUREMENTS]-(rm:RoomMeasurement) OPTIONAL MATCH (rm:RoomMeasurement)-[rrm:ROOM_ROOMMEASUREMENTS]-(r:Room) OPTIONAL MATCH (rs:RoomSetting)-[rrs:ROOM_ROOMSETTINGS]-(r:Room) OPTIONAL MATCH (i:Iteration)-[ium:ITERATION_UNITMEASUREMENT]-(um:UnitMeasurement) OPTIONAL MATCH (i:Iteration)-[ius:ITERATION_UNITSETTING]-(us:UnitSetting) RETURN i, irs, rs, irm, rm, ium, um, ius, us, rrm, rrs, r")
    List<Iteration> findIterationsForUnit(String unitId);
}
