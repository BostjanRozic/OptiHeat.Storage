package optiheat.storage.model;


import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

public class Unit
{
    @Id
    public String id;
    public String name;

    @Relationship(type = "USER_UNITS", direction = Relationship.INCOMING)
    public User user;

    @Relationship(type = "UNIT_ROOMS", direction = Relationship.UNDIRECTED)
    public Set<Room> rooms;
}
