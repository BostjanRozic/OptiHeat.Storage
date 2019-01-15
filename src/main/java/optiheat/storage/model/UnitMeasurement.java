package optiheat.storage.model;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

public class UnitMeasurement
{
    @Id
    public String id;

    public Double t_Out;
    public Double t_Heat;

    @Relationship(type = "UNIT_UNITMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public Unit unit;

    @Relationship(type = "ITERATION_UNITMEASUREMENT", direction = Relationship.UNDIRECTED)
    public Iteration iteration;
}
