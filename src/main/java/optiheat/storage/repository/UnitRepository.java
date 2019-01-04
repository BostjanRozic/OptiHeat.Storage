package optiheat.storage.repository;

import optiheat.storage.model.Unit;
import org.springframework.data.repository.CrudRepository;

public interface UnitRepository extends CrudRepository<Unit, Long>
{
    Unit findById(String id);
}
