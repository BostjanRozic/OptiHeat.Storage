package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class RoomSetting
{
    @Id
    public String id;
    public Double t_setPoint;
    public Double valveLevel;

    @Relationship(type = "ROOM_ROOMMEASUREMENT", direction = Relationship.UNDIRECTED)
    public Room room;

    @Relationship(type = "ITERATION_ROOMMEASUREMENT", direction = Relationship.UNDIRECTED)
    public Iteration iteration;
}
