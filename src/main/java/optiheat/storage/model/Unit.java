package optiheat.storage.model;


import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

public class Unit
{
    @Id
    public String id;
    public String name;

    @Relationship(type = "USER_UNITS", direction = Relationship.INCOMING)
    public User user;

    @Relationship(type = "UNIT_ROOMS", direction = Relationship.UNDIRECTED)
    public List<Room> rooms;

    @Relationship(type = "UNIT_UNITMEASUREMENTS", direction = Relationship.UNDIRECTED)
    public List<UnitMeasurement> unitMeasurements;

    @Relationship(type = "UNIT_UNITSETTINGS", direction = Relationship.UNDIRECTED)
    public List<UnitSetting> unitSettings;

    @Relationship(type = "UNIT_ITERATIONS", direction = Relationship.UNDIRECTED)
    public List<Iteration> iterations;
}
