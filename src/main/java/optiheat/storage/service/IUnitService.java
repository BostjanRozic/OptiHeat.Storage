package optiheat.storage.service;

import optiheat.storage.model.Unit;

public interface IUnitService
{
    void createUnit(String userId, Unit unit) throws Exception;
    void deleteUnit(String unitId);
    Unit getUnit(String unitId);
}
