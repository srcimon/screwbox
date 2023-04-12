package io.github.srcimon.screwbox.core.entities.components;

import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class CollisionSensorComponent implements Component {

    private static final long serialVersionUID = 1L;

    public List<Entity> collidedEntities = new ArrayList<>();

}
