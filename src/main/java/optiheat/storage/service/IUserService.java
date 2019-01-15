package optiheat.storage.service;

import optiheat.storage.model.User;

public interface IUserService
{
    void createUser(User user);
    void deleteUser(String userId);
    User getUser(String id);
}
