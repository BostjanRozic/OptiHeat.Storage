package optiheat.storage.controller;

import optiheat.storage.model.Unit;
import optiheat.storage.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnitServiceController
{
    @Autowired
    SpecificationService specificationService;

    @RequestMapping(value = "/Storage/SpecificationService/createUnit", method = RequestMethod.POST)
    public void createUser(@RequestBody Unit unit) throws Exception
    {
        specificationService.createUnit(unit);
    }
}
