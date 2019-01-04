package optiheat.storage.service;

import optiheat.storage.model.Unit;

public interface IUnitService
{
    void deleteAll();
    Unit getUnit(String id);
}
