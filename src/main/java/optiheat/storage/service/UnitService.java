package optiheat.storage.service;

import optiheat.storage.model.Unit;
import optiheat.storage.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnitService implements IUnitService
{
    private final UnitRepository unitRepository;
    public UnitService(UnitRepository unitRepository)
    {
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = false)
    public void deleteAll()
    {
        unitRepository.deleteAll();
    }

    @Override
    public Unit getUnit(String id)
    {
        return unitRepository.findById(id);
    }
}
