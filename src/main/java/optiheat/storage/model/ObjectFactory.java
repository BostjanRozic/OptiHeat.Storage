package optiheat.storage.model;

public class ObjectFactory
{
    /**
     * This method is not intended to preserve original objects. It is meant to be used by controller, just before the return
     * for purposes of avoiding cyclic JSON model errors
     * @param unit
     * @return
     */
    public static Unit createDirectedGraph(Unit unit)
    {
        if (unit.user != null)
            unit.user.units = null;
        if (unit.rooms != null)
        {
            for (Room room : unit.rooms)
            {
                room.unit = null;
                if (room.roomMeasurements != null)
                {
                    for (RoomMeasurement rm : room.roomMeasurements)
                    {
                        if (rm.iteration != null)
                        {
                            rm.iteration.roomMeasurements = null;
                            rm.iteration.unit = null;
                        }
                        rm.room = null;
                    }
                }
                if (room.roomSettings != null)
                {
                    for (RoomSetting rs : room.roomSettings)
                    {
                        if (rs.iteration != null)
                        {
                            rs.iteration.roomSettings = null;
                            rs.iteration.unit = null;
                        }
                        rs.room = null;
                    }
                }
            }
        }
        if (unit.unitMeasurements != null)
        {
            for (UnitMeasurement um : unit.unitMeasurements)
            {
                if (um.iteration != null)
                {
                    um.iteration.unitMeasurement = null;
                    um.iteration.unit = null;
                }
                um.unit = null;
            }
        }
        if (unit.unitSettings != null)
        {
            for (UnitSetting us : unit.unitSettings)
            {
                if (us.iteration != null)
                {
                    us.iteration.unitSetting = null;
                    us.iteration.unit = null;
                }
                us.unit = null;
            }
        }
        return unit;
    }

    public static Iteration createDirectedGraph(Iteration iteration)
    {
        if (iteration.unit != null)
        {
            iteration.unit.iterations = null;
            iteration.unit.name = null;
            iteration.unit.user = null;
            iteration.unit.unitSettings = null;
            iteration.unit.unitMeasurements = null;
            iteration.unit.rooms = null;
        }
        if (iteration.unitMeasurement != null)
        {
            iteration.unitMeasurement.iteration = null;
            iteration.unitMeasurement.unit = null;
        }
        if (iteration.unitSetting != null)
        {
            iteration.unitSetting.iteration = null;
            iteration.unitSetting.unit = null;
        }
        if (iteration.roomSettings != null)
        {
            for (RoomSetting rs : iteration.roomSettings)
            {
                rs.iteration = null;
                if (rs.room != null)
                {
                    rs.room.name = null;
                    rs.room.roomSettings = null;
                    rs.room.roomMeasurements = null;
                    rs.room.unit = null;
                }
            }
        }
        if (iteration.roomMeasurements != null)
        {
            for (RoomMeasurement rm : iteration.roomMeasurements)
            {
                rm.iteration = null;
                if (rm.room != null)
                {
                    rm.room.name = null;
                    rm.room.roomSettings = null;
                    rm.room.roomMeasurements = null;
                    rm.room.unit = null;
                }
            }
        }
        return iteration;
    }
}
