package optiheat.storage.controller;

import optiheat.storage.model.Iteration;
import optiheat.storage.service.DWHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IterationServiceController
{
    @Autowired
    DWHService DWHService;

    @RequestMapping(value = "/Storage/DWHService/createIteration", method = RequestMethod.POST)
    public void createIteration(@RequestBody Iteration iteration) throws Exception
    {
        DWHService.createIteration(iteration);
    }
}
