package optiheat.storage.controller;

import optiheat.storage.model.Unit;
import optiheat.storage.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnitServiceController
{
    @Autowired
    UnitService unitService;

    @RequestMapping(value = "/Storage/UnitService/createUnit", method = RequestMethod.POST)
    public void createUser(@RequestBody Unit unit) throws Exception
    {
        unitService.createUnit(unit);
    }
}
