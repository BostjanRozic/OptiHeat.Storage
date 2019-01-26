package optiheat.storage.service;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.model.Iteration;
import optiheat.storage.model.UnitMeasurement;
import optiheat.storage.repository.IterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DWHService implements IDWHService
{
    @Autowired
    private IterationRepository iterationRepository;

    @Autowired
    private UnitMeasurementRepository unitMeasurementRepository;



    @Transactional
    public void createIteration(Iteration iteration)
    {
        // payload validation
        if (iteration == null || iteration.unit == null || iteration.unit.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        iterationRepository.save(iteration);
    }

    public List<Iteration> getIterations(String unitId)
    {
        // payload validation
        if (unitId == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        List<Iteration> iterations = iterationRepository.findIterationsForUnit(unitId);
        if (iterations != null)
        {
            for (Iteration iteration : iterations)
            {
                if (iteration.id != null)
                    continue;
                iteration = iterationRepository.findById(iteration.id);

            }
        }
    }
}
