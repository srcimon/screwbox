package de.suzufa.screwbox.core.entityengine.components;

import java.util.ArrayList;

import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.entityengine.Entity;

public class CollisionSensorComponent implements Component {

    private static final long serialVersionUID = 1L;

    public ArrayList<Entity> collidedEntities = new ArrayList<>();

}
