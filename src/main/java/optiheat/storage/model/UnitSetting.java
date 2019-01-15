package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class UnitSetting
{
    @Id
    public String id;

    public double t_min;
    public double t_max;

    @Relationship(type = "UNIT_UNITSETTINGS", direction = Relationship.UNDIRECTED)
    public Unit unit;

    @Relationship(type = "ITERATION_UNITSETTING", direction = Relationship.UNDIRECTED)
    public Iteration iteration;
}
