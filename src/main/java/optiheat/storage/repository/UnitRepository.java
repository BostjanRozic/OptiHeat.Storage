package optiheat.storage.repository;

import optiheat.storage.model.Unit;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit, Long>
{
    Unit findById(String id);

    /*@Query("MATCH (u:Unit {id:{unitId}})-[uus:UNIT_UNITSETTINGS]-(us:UnitSetting)-[ius:ITERATION_UNITSETTING]-(i:Iteration) return u, uus, us, ius, i ORDER BY i.sequence DESC LIMIT 1")
    Unit getUnitWithLastUnitSetting(String unitId);*/

    @Query("MATCH (u:Unit {id:{unitId}}) OPTIONAL MATCH (u)-[uus:UNIT_UNITSETTINGS]-(us:UnitSetting)--(i:Iteration) return u, uus, us ORDER BY i.sequence DESC LIMIT 1")
    Unit getUnitWithLastUnitSetting(String unitId);

    @Query("MATCH (u:Unit {id:{unitId}}) OPTIONAL MATCH (u)-[ur:UNIT_ROOMS]-(r:Room) return u, ur, r")
    Unit getUnitWithRooms(String unitId);

    @Query("MATCH (u:Unit)--(:User {id:{userId}}) return u")
    List<Unit> getUnitsFlat(String userId);

    @Query("MATCH (r:Room)-[:UNIT_ROOMS]-(:Unit {id:{unitId}}) DETACH DELETE r")
    void deleteRoomsForUnit(String unitId);

    @Query("MATCH (u:UnitMeasurement)-[:UNIT_UNITMEASUREMENTS]-(:Unit {id:{unitId}}) DELETE u")
    void deleteAllMeasurementsForUnit(String unitId);

    @Query("MATCH (u:UnitSetting)-[:UNIT_UNITSETTINGS]-(:Unit {id:{unitId}}) DELETE u")
    void deleteAllSettingsForUnit(String unitId);

    @Query("MATCH (u:Unit {id:{unitId}})")
    void deleteUnit(String unitId);
}
