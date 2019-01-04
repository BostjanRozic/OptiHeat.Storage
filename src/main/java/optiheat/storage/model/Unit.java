package optiheat.storage.model;


import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class Unit
{
    @Id
    public String id;
    public String name;

    @Relationship(type = "UNIT", direction = Relationship.UNDIRECTED)
    public User user;
}
