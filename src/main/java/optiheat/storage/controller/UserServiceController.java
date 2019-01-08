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

    @RequestMapping(value = "/getUsr", method = RequestMethod.POST)
    @ResponseBody
    public User getUser(@RequestBody String userId)
    {
        return userService.getUser(userId);
    }
}
