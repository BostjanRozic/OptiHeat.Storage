package optiheat.storage.service;

import optiheat.storage.model.User;
import optiheat.storage.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = false)
    public void createUser(User user)
    {
        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public void deleteAll()
    {
        userRepository.deleteAll();
    }

    public User getUser(String id)
    {
        return userRepository.findById(id);
    }
}
