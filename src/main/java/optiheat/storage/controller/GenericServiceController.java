package optiheat.storage.controller;

import optiheat.storage.model.User;
import optiheat.storage.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericServiceController
{
    @Autowired
    GenericService genericService;

    @RequestMapping(value = "/Storage/GenericService/deleteEntireDatabase", method = RequestMethod.DELETE)
    public @ResponseBody void deleteEntireDatabase()
    {
        genericService.deleteEntireDatabase();
    }
}
