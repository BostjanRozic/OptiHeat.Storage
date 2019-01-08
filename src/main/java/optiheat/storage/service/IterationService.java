package optiheat.storage.service;

import optiheat.storage.model.Iteration;
import optiheat.storage.repository.IterationRepository;
import org.springframework.stereotype.Service;

@Service
public class IterationService implements IIterationService
{
    private final IterationRepository iterationRepository;
    public IterationService(IterationRepository iterationRepository)
    {
        this.iterationRepository = iterationRepository;
    }
    @Override
    public void createIteration(Iteration iteration)
    {
        iterationRepository.save(iteration);
    }
}
