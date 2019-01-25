package optiheat.storage.repository;

import optiheat.storage.model.RoomSetting;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomSettingRepository extends CrudRepository<RoomSetting, Long>
{
    RoomSetting findById(String id);

    @Query("MATCH (rs:RoomSetting) return rs")
    public List<RoomSetting> getAllRoomSettings();
}
