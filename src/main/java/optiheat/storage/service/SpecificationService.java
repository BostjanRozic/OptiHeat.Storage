package optiheat.storage.service;

import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.repository.RoomRepository;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecificationService implements ISpecificationService
{
    @Autowired
    private RoomRepository roomRepository;
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    public SpecificationService(UnitRepository unitRepository, UserRepository userRepository)
    {
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUnit(String userId, Unit unit)
    {
        User user = userRepository.findById(userId);
        if (user == null)
            throw new NotFoundException("User with id: " + userId + " does not exist in database");

        unit.user = user;
        unitRepository.save(unit);
    }

    @Transactional
    public void createRoom(Room room)
    {
        Unit unit = unitRepository.findById(room.unit.id);
        if (unit == null)
            throw new NotFoundException("Unit with id: " + unit.id + " does not exist in database");

        room.unit = unit;
        roomRepository.save(room);
    }

    @Transactional
    public void updateUnit(Unit unit)
    {
        Unit existingUnit = unitRepository.findById(unit.id);
        if (existingUnit == null)
            throw new NotFoundException("Unit with id: " + unit.id + " does not exist in database");

        if (unit.name != null)
            existingUnit.name = unit.name;

        unitRepository.save(existingUnit);
    }

    @Transactional
    public void updateRoom(Room room)
    {
        Room existingRoom = roomRepository.findById(room.id);
        if (existingRoom == null)
            throw new NotFoundException("Room with id: " + room.id + " does not exist in database");

        if (room.name != null)
            existingRoom.name = room.name;

        roomRepository.save(existingRoom);
    }

    @Transactional(readOnly = false)
    public void deleteUnit(String unitId)
    {
        Unit existingUnit = unitRepository.findById(unitId);
        if (existingUnit == null)
            throw new NotFoundException("Unit with id: " + unitId + " does not exist in database");

        // remove all related rooms
        if (existingUnit.rooms != null)
        {
            for (Room room : existingUnit.rooms)
            {
                deleteRoom(room.id);
            }
        }

        unitRepository.deleteAllMeasurementsForUnit(unitId);
        unitRepository.deleteAllSettingsForUnit(unitId);
        unitRepository.deleteUnit(unitId);
    }

    @Transactional(readOnly = false)
    public void deleteRoom(String roomId)
    {
        Room existingRoom = roomRepository.findById(roomId);
        if (existingRoom == null)
            throw new NotFoundException("Room with id: " + roomId + " does not exist in database");

        roomRepository.deleteAllMeasurementsForRoom(roomId);
        roomRepository.deleteAllSettingsForRoom(roomId);
        roomRepository.deleteRoom(roomId);
    }

    public Unit getUnit(String unitId)
    {
        return unitRepository.findById(unitId);
    }

    public Room getRoom(String roomId)
    {
        return roomRepository.findById(roomId);
    }
}
