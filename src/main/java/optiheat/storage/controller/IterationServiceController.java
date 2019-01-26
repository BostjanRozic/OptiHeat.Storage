package optiheat.storage.controller;

import optiheat.storage.model.Iteration;
import optiheat.storage.service.DWHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/Storage/DWHServiceService/getIteration", method = RequestMethod.GET)
    @ResponseBody
    public Iteration getUnit(@RequestParam("unitId") String unitId)
    {
        Iteration iteration =
        if (unit != null)
            unit = ObjectFactory.createDirectedGraph(unit);
        return unit;
    }
}
