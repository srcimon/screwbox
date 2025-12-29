package dev.screwbox.platformer.map;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.tiled.Map;

public class MapGravity implements Blueprint<Map> {

    @Override
    public Entity assembleFrom(final Map map) {
        final Vector gravity = Vector.of(
                map.properties().tryGetDouble("gravity-x").orElse(0.0),
                map.properties().tryGetDouble("gravity-y").orElse(700.0));
        return new Entity().add(new GravityComponent(gravity));
    }

}
