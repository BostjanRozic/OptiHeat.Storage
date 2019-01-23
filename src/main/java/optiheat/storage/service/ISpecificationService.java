package optiheat.storage.service;

import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;

public interface ISpecificationService
{
    void createUnit(String userId, Unit unit);
    void createRoom(Room room);
    void updateUnit(Unit unit);
    void updateRoom(Room room);
    void deleteUnit(String unitId);
    void deleteRoom(String roomId);
    Unit getUnit(String unitId);
    Room getRoom(String roomId);
}
