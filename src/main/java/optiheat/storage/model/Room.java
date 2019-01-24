package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;


public class Room
{
    @Id
    public String id;
    public String name;

    @Relationship(type = "UNIT_ROOMS", direction = Relationship.UNDIRECTED)
    public Unit unit;

    @Relationship(type = "ROOM_ROOMMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public List<RoomMeasurement> roomMeasurements;

    @Relationship(type = "ROOM_ROOMSETTINGS", direction = Relationship.UNDIRECTED)
    public List<RoomSetting> roomSettings;
}
