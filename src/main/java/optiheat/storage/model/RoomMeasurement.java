package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;


public class RoomMeasurement
{
    @Id
    public String id;
    public Double t;

    @Relationship(type = "ROOM_ROOMMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public Room room;

    @Relationship(type = "ITERATION_ROOMMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public Iteration iteration;
}
