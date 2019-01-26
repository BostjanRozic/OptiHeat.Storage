package optiheat.storage.service;

import optiheat.storage.model.Iteration;

import java.util.List;

public interface IDWHService
{
    void createIteration(Iteration iteration);
    List<Iteration> getIterations(String unitId);
}
