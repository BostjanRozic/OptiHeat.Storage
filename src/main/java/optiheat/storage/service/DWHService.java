package optiheat.storage.service;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.model.Iteration;
import optiheat.storage.model.RoomMeasurement;
import optiheat.storage.model.RoomSetting;
import optiheat.storage.model.UnitMeasurement;
import optiheat.storage.repository.IterationRepository;
import optiheat.storage.repository.RoomMeasurementRepository;
import optiheat.storage.repository.RoomSettingRepository;
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
    private RoomSettingRepository roomSettingRepository;

    @Autowired
    private RoomMeasurementRepository roomMeasurementRepository;

    @Transactional
    public int createIteration(Iteration iteration)
    {
        // payload validation
        if (iteration == null || iteration.unit == null || iteration.unit.id == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        Iteration latestIteration = iterationRepository.findLatestIterationForUnit(iteration.unit.id);
        if (latestIteration == null)
        {
            iteration.sequence = 0;
        }
        else
        {
            iteration.sequence = latestIteration.sequence + 1;
        }
        iterationRepository.save(iteration);
        return iteration.sequence;
    }

    public List<Iteration> getIterations(String unitId)
    {
        // payload validation
        if (unitId == null)
            throw new BadRequestException("Invalid payload. One of the input arguments is null or missing attributes");

        List<Iteration> iterations = iterationRepository.findIterationsForUnit(unitId);
        /*if (iterations != null)
        {
            for (Iteration iteration : iterations)
            {
                if (iteration.id != null)
                    continue;
                iteration = iterationRepository.findById(iteration.id);
                for (RoomMeasurement roomMeasurement : iteration.roomMeasurements)
                {
                    roomMeasurement = roomMeasurementRepository.findById(roomMeasurement.id);
                }
                for (RoomSetting roomSetting : iteration.roomSettings)
                {
                    roomSetting = roomSettingRepository.findById(roomSetting.id);
                }
            }
        }*/

        return iterations;
    }
}
