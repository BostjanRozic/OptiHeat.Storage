package optiheat.storage.repository;

import optiheat.storage.model.Room;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long>
{
    Room findById(String id);

    @Query("MATCH (r:RoomMeasurement)-[:ROOM_ROOMMEASUREMENTS]-(:Room {id:{roomId}}) DELETE r")
    void deleteAllMeasurementsForRoom(String roomId);

    @Query("MATCH (r:RoomSetting)-[:ROOM_ROOMSETTINGS]-(:Room {id:{roomId}}) DELETE r")
    void deleteAllSettingsForRoom(String roomId);

    @Query("MATCH (r:Room {id:{roomId}}) DELETE r")
    void deleteRoom(String roomId);
}
