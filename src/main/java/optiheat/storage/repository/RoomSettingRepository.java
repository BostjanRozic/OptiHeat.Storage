package optiheat.storage.repository;

import optiheat.storage.model.RoomSetting;
import org.springframework.data.repository.CrudRepository;

public interface RoomSettingRepository extends CrudRepository<RoomSetting, Long>
{
    RoomSetting findById(String id);
}
