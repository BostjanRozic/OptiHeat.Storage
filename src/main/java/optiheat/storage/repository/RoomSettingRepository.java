package optiheat.storage.repository;

import optiheat.storage.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomSettingRepository extends CrudRepository<Room, Long>
{
    Room findById(String id);
}
