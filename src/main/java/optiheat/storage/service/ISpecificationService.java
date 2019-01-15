package optiheat.storage.service;

import optiheat.storage.model.Unit;

public interface ISpecificationService
{
    void createUnit(Unit unit) throws Exception;
    void deleteUnit(String unitId);
    Unit getUnit(String unitId);
}
