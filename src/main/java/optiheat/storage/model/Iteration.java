package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Date;
import java.util.List;

public class Iteration
{
    @Id
    public String id;
    public Date datetime;
    public int sequence;

    @Relationship(type = "UNIT_ITERATIONS", direction = Relationship.UNDIRECTED)
    public Unit unit;

    @Relationship(type = "ITERATION_ROOMMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public List<RoomMeasurement> roomMeasurements;

    @Relationship(type = "ITERATION_ROOMSETTINGS", direction = Relationship.UNDIRECTED)
    public List<RoomSetting> roomSettings;

    @Relationship(type = "ITERATION_UNITMEASUREMENT", direction = Relationship.UNDIRECTED)
    public UnitMeasurement unitMeasurement;

    @Relationship(type = "ITERATION_UNITSETTING", direction = Relationship.UNDIRECTED)
    public UnitSetting unitSetting;

    public int getSequence()
    {
        return sequence;
    }
}
