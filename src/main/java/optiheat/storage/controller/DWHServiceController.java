package optiheat.storage.controller;

import optiheat.storage.model.Iteration;
import optiheat.storage.model.ObjectFactory;
import optiheat.storage.service.DWHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DWHServiceController
{
    @Autowired
    DWHService DWHService;

    @RequestMapping(value = "/Storage/DWHService/createIteration", method = RequestMethod.POST)
    public void createIteration(@RequestBody Iteration iteration) throws Exception
    {
        DWHService.createIteration(iteration);
    }

    @RequestMapping(value = "/Storage/DWHService/getIterations", method = RequestMethod.GET)
    @ResponseBody
    public List<Iteration> getIterations(@RequestParam("unitId") String unitId)
    {
        List<Iteration> iterations = DWHService.getIterations(unitId);
        if (iterations == null)
            return null;
        List<Iteration> rtnIterations = new ArrayList<>();
        for (Iteration iteration : iterations)
        {
            Iteration directedIteration = ObjectFactory.createDirectedGraph(iteration);
            rtnIterations.add(directedIteration);
        }
        return rtnIterations;
    }
}
