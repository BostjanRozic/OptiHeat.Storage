package optiheat.storage.service;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.model.User;
import optiheat.storage.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService implements IUserService
{
    private String userId;
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public void setUser(String userId)
    {
        this.userId = userId;
    }

    @Transactional(readOnly = false)
    public void createUser(User user)
    {
        if (user == null)
            throw new BadRequestException("Empty Payload");
        if (user.units != null)
            throw new BadRequestException("Payload is expected to be FLAT");
        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public void deleteAll()
    {
        userRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteUser(String userId)
    {
        userRepository.deleteAllRoomsForUser(userId);
        userRepository.deleteAllUnitsForUser(userId);
        userRepository.deleteUser(userId);
    }

    public User getUser(String id)
    {
        return userRepository.findById(id);
    }
}
