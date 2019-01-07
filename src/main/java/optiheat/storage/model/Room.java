package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class Room
{
    @Id
    public String id;
    public String name;

    @Relationship(type = "UNIT_ROOMS", direction = Relationship.UNDIRECTED)
    public Unit unit;
}
