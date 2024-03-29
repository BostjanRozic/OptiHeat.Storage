package optiheat.storage.controller;

import optiheat.storage.model.ObjectFactory;
import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpecificationServiceController
{
    @Autowired
    SpecificationService specificationService;

    @RequestMapping(value = "/Storage/SpecificationService/createUnit", method = RequestMethod.POST)
    public void createUser(@RequestParam("userId") String userId, @RequestBody Unit unit)
    {
        specificationService.createUnit(userId, unit);
    }

    @RequestMapping(value = "/Storage/SpecificationService/createRoom", method = RequestMethod.POST)
    public void createRoom(@RequestBody Room room)
    {
        specificationService.createRoom(room);
    }

    @RequestMapping(value = "/Storage/SpecificationService/updateUnit", method = RequestMethod.PUT)
    public void updateUnit(@RequestBody Unit unit)
    {
        specificationService.updateUnit(unit);
    }

    @RequestMapping(value = "/Storage/SpecificationService/updateRoom", method = RequestMethod.PUT)
    public void updateRoom(@RequestBody Room room)
    {
        specificationService.updateRoom(room);
    }

    @RequestMapping(value = "/Storage/SpecificationService/deleteUnit", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUnit(@RequestParam("unitId") String unitId)
    {
        specificationService.deleteUnit(unitId);
    }

    @RequestMapping(value = "/Storage/SpecificationService/deleteRoom", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteRoom(@RequestParam("roomId") String roomId)
    {
        specificationService.deleteRoom(roomId);
    }

    @RequestMapping(value = "/Storage/SpecificationService/getUnit", method = RequestMethod.GET)
    @ResponseBody
    public Unit getUnit(@RequestParam("unitId") String unitId)
    {
        Unit unit = specificationService.getUnit(unitId);
        if (unit != null)
            unit = ObjectFactory.createDirectedGraph(unit);
        return unit;
    }

    @RequestMapping(value = "/Storage/SpecificationService/getUnits", method = RequestMethod.GET)
    @ResponseBody
    public List<Unit> getUnits(@RequestParam("userId") String userId)
    {
        List<Unit> directedUnits = null;
        List<Unit> units = specificationService.getUnits(userId);
        if (units != null)
        {
            directedUnits = new ArrayList<>();
            for(Unit unit : units)
            {
                directedUnits.add(ObjectFactory.createDirectedGraph(unit));
            }
        }
        return directedUnits;
    }
}
