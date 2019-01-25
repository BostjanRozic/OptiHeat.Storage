package optiheat.storage.service;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.*;
import optiheat.storage.repository.RoomRepository;
import optiheat.storage.repository.RoomSettingRepository;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class SpecificationService implements ISpecificationService
{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomSettingRepository roomSettingRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UserRepository userRepository;
    /*public SpecificationService(UnitRepository unitRepository, UserRepository userRepository)
    {
        this.unitRepository = unitRepository;
        this.userRepository = userRepository;
    }*/

    @Transactional
    public void createUnit(String userId, Unit unit)
    {
        // payload validation
        if (userId == null || unit == null || unit.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        if (unitRepository.findById(unit.id) != null)
            throw new ConflictException("Unit with the specified ID: " + unit.id + " already exists in DB. Will not create a new one...");

        User user = userRepository.findById(userId);
        if (user == null)
            throw new NotFoundException("User with id: " + userId + " does not exist in database");

        unit.user = user;
        unitRepository.save(unit);
    }

    @Transactional
    public void createRoom(Room room)
    {
        // payload validation
        if (room == null || room.id == null || room.unit == null || room.unit.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        Unit unit = unitRepository.findById(room.unit.id);
        if (unit == null)
            throw new NotFoundException("Unit with id: " + room.unit.id + " does not exist in database");

        if (roomRepository.findById(room.id) != null)
            throw new ConflictException("Room with the specified ID: " + room.id + " already exists in DB. Will not create a new one...");

        room.unit = unit;
        roomRepository.save(room);
    }

    @Transactional
    public void updateUnit(Unit unit)
    {
        // payload validation
        if (unit == null || unit.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        Unit existingUnit = unitRepository.findById(unit.id);
        if (existingUnit == null)
            throw new NotFoundException("Unit with id: " + unit.id + " does not exist in database");

        if (unit.name != null)
            existingUnit.name = unit.name;
            //unitRepository.setName(unit.id, unit.name);
        unitRepository.save(existingUnit);
    }

    @Transactional
    public void updateRoom(Room room)
    {
        // payload validation
        if (room == null || room.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

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
        // payload validation
        if (unitId == null)
            throw new BadRequestException("Invalid request - query argument unitId missing or null");

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
        // payload validation
        if (roomId == null)
            throw new BadRequestException("Invalid request - query argument roomId missing or null");

        Room existingRoom = roomRepository.findById(roomId);
        if (existingRoom == null)
            throw new NotFoundException("Room with id: " + roomId + " does not exist in database");

        roomRepository.deleteAllMeasurementsForRoom(roomId);
        roomRepository.deleteAllSettingsForRoom(roomId);
        roomRepository.deleteRoom(roomId);
    }

    /**
     * Iz specifikacije.
     * Payload obsega:
     * - Unit z vsemi atributi
     * - V relaciji zadnji veljavni UnitSetting objekt
     * - Vsi pripadajoči Room objekti
     * - V relaciji na vsak Room objekt pripadajoči zadnji veljavni RoomSetting objekt
     * @param unitId
     * @return
     */
    public Unit getUnit(String unitId)
    {
        // payload validation
        if (unitId == null)
            throw new BadRequestException("Invalid request - query argument roomId missing or null");

        //Unit unit = unitRepository.findById(unitId);
        Unit unit = unitRepository.getUnitWithLastUnitSetting(unitId);
        if (unit == null)
            return null;

        unit = unitRepository.getUnitWithRooms(unitId);
        if (unit.rooms != null)
        {
            for (Room room : unit.rooms)
            {
                room = roomRepository.getRoomWithLastRoomSetting(room.id);
            }
        }
        return unit;
    }

    /**
     * Iz specifikacije:
     * Payload obsega seznam vseh Unit objektov, vsak izmed njih z vsemi atributi a brez vsakršnih relacij.
     * @param userId
     * @return
     */
    public List<Unit> getUnits(String userId)
    {
        // payload validation
        if (userId == null)
            throw new BadRequestException("Invalid request - query argument roomId missing or null");

        List<Unit> units = unitRepository.getUnitsFlat(userId);
        return units;
    }
}
