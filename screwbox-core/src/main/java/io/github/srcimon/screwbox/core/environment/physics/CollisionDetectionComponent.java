package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetectionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public List<Entity> collidedEntities = new ArrayList<>();

}
