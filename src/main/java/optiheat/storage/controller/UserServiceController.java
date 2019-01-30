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

    @RequestMapping(value = "/Storage/UserService/createUser", method = RequestMethod.POST)
    public void createUser(@RequestBody User user)
    {
        userService.createUser(user);
    }

    @RequestMapping(value = "/Storage/UserService/deleteUser", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@RequestParam("userId") String userId)
    {
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "/Storage/UserService/getUser", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@RequestParam("userId") String userId)
    {
        User user = userService.getUser(userId);
        if (user != null)
            user.units = null;
        return user;
    }
}
