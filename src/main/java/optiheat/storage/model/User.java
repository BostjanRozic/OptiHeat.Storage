package optiheat.storage.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Set;

@NodeEntity
public class User
{
    @Id
    public String id;

    @Relationship(type = "USER_UNITS", direction = Relationship.OUTGOING)
    public Set<Unit> units;
}
