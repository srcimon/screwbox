package de.suzufa.screwbox.core.entityengine.components;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.entityengine.Entity;

public class CollisionSensorComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final List<Entity> collidedEntities = new ArrayList<>();

}
