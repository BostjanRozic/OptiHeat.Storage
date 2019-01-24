package optiheat.storage.controller;

import optiheat.storage.model.ObjectFactory;
import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/Storage/SpecificationService/updateUnit", method = RequestMethod.PATCH)
    public void updateUser(@RequestBody Unit unit)
    {
        specificationService.updateUnit(unit);
    }

    @RequestMapping(value = "/Storage/SpecificationService/updateRoom", method = RequestMethod.PATCH)
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
        return ObjectFactory.createDirectedGraph(unit);
    }

    @RequestMapping(value = "/Storage/SpecificationService/getRoom", method = RequestMethod.GET)
    @ResponseBody
    public Room getRoom(@RequestParam("roomId") String roomId)
    {
        return specificationService.getRoom(roomId);
    }
}
