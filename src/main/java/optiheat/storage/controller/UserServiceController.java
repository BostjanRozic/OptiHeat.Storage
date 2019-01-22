package optiheat.storage.controller;

import optiheat.storage.model.User;
import optiheat.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserServiceController
{
    @Autowired
    UserService userService;

    @RequestMapping(value = "/Storage/UserService/getUser", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(String userId)
    {
        return userService.getUser(userId);
    }

    @RequestMapping(value = "/Storage/UserService/createUser", method = RequestMethod.POST)
    public void createUser(@RequestBody User user)
    {
        userService.createUser(user);
    }

    @RequestMapping(value = "/addRoom", method = RequestMethod.POST)
    public @ResponseBody void createUser()
    {
        int a = 1;
    }
}
