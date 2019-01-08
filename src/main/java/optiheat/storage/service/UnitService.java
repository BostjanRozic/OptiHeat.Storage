package optiheat.storage.service;

import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnitService implements IUnitService
{
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    public UnitService(UnitRepository unitRepository, UserRepository userRepository)
    {
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createUnit(Unit unit) throws Exception
    {
        User user = userRepository.findById(unit.user.id);
        if (user == null)
            throw new Exception ("User with id: " + unit.user.id + " does not exist in database");
        unit.user = user;
        unitRepository.save(unit);
    }

    @Transactional(readOnly = false)
    public void deleteUnit(String unitId)
    {
        unitRepository.deleteRoomsForUnit(unitId);
        unitRepository.deleteUnit(unitId);
    }

    @Override
    public Unit getUnit(String id)
    {
        return unitRepository.findById(id);
    }
}
