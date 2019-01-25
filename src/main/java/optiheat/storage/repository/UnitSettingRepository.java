package optiheat.storage.repository;

import optiheat.storage.model.UnitSetting;
import org.springframework.data.repository.CrudRepository;

public interface UnitSettingRepository extends CrudRepository<UnitSetting, Long>
{
    UnitSetting findById(String id);
}
