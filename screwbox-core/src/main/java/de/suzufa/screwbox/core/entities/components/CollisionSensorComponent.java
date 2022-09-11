package de.suzufa.screwbox.core.entities.components;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.entities.Entity;

public class CollisionSensorComponent implements Component {

    private static final long serialVersionUID = 1L;

    public List<Entity> collidedEntities = new ArrayList<>();

}
