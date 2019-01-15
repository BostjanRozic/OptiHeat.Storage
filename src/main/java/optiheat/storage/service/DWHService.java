package optiheat.storage.service;

import optiheat.storage.model.Iteration;
import optiheat.storage.repository.IterationRepository;
import org.springframework.stereotype.Service;

@Service
public class DWHService implements IDWHService
{
    private final IterationRepository iterationRepository;
    public DWHService(IterationRepository iterationRepository)
    {
        this.iterationRepository = iterationRepository;
    }
    @Override
    public void createIteration(Iteration iteration)
    {
        iterationRepository.save(iteration);
    }
}
