package optiheat.storage.service;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.controller.exception.InternalServerErrorException;
import optiheat.storage.model.User;
import optiheat.storage.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService implements IUserService
{
    private String userId;
    @Autowired
    private UserRepository userRepository;
    /*public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }*/

    public void setUser(String userId)
    {
        this.userId = userId;
    }

    @Transactional(readOnly = false)
    public void createUser(User user)
    {
        // Verify pre-conditions
        if (user == null)
            throw new BadRequestException("Empty Payload");
        if (user.id == null)
            throw new BadRequestException("user.Id is Empty");
        if (user.units != null)
            throw new BadRequestException("Payload is expected to be FLAT");
        if (userRepository.findById(user.id) != null)
            throw new BadRequestException("User with id: " + user.id + " already exists in DB");

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
        // Verify pre-conditions
        if (userId == null)
            throw new BadRequestException("user.Id is Empty");
        if (getUser(userId) == null)
            throw new InternalServerErrorException("User with ID: " +  userId + " could not be deleted because it was not found in the database");

        userRepository.deleteAllRoomsForUser(userId);
        userRepository.deleteAllUnitsForUser(userId);
        userRepository.deleteUser(userId);
    }

    public User getUser(String userId)
    {
        if (userId == null)
            throw new BadRequestException("user.Id is Empty");

        return userRepository.findById(userId);
    }
}
