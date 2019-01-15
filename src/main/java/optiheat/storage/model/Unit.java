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

    @Relationship(type = "UNIT_UNITMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public Set<UnitMeasurement> unitMeasurements;

    @Relationship(type = "UNIT_UNITSETTINGS", direction = Relationship.UNDIRECTED)
    public Set<UnitSetting> unitSettings;

    @Relationship(type = "UNIT_ITERATIONS", direction = Relationship.UNDIRECTED)
    public Set<Iteration> iterations;
}
