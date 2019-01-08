package optiheat.storage.controller;

import optiheat.storage.model.Iteration;
import optiheat.storage.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IterationServiceController
{
    @Autowired
    IterationService iterationService;

    @RequestMapping(value = "/Storage/IterationService/createIteration", method = RequestMethod.POST)
    public void createIteration(@RequestBody Iteration iteration) throws Exception
    {
        iterationService.createIteration(iteration);
    }
}
