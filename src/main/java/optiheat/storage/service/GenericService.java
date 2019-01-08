package optiheat.storage.service;

import optiheat.storage.repository.GenericRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenericService implements IGenericService
{
    private final GenericRepository genericRepository;

    public GenericService(GenericRepository genericRepository)
    {
        this.genericRepository = genericRepository;
    }
    @Transactional
    public void deleteEntireDatabase()
    {
        genericRepository.deleteAll();
    }
}
