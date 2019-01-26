package optiheat.storage.repository;

import optiheat.storage.model.RoomMeasurement;
import org.springframework.data.repository.CrudRepository;

public interface RoomMeasurementRepository extends CrudRepository<RoomMeasurement, Long>
{
    RoomMeasurement findById(String id);
}
