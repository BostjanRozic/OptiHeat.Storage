package optiheat.storage.repository;

import optiheat.storage.model.Unit;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

public interface UnitRepository extends CrudRepository<Unit, Long>
{
    Unit findById(String id);

    @Query("MATCH (r:Room)-[:UNIT_ROOMS]-(:Unit {id:{unitId}}) DETACH DELETE r")
    void deleteRoomsForUnit(String unitId);

    @Query("MATCH (u:UnitMeasurement)-[:UNIT_UNITMEASUREMENTS]-(:Unit {id:{unitId}}) DELETE u")
    void deleteAllMeasurementsForUnit(String unitId);

    @Query("MATCH (u:UnitSetting)-[:UNIT_UNITSETTINGS]-(:Unit {id:{unitId}}) DELETE u")
    void deleteAllSettingsForUnit(String unitId);

    @Query("MATCH (u:Unit {id:{unitId}})")
    void deleteUnit(String unitId);
}
