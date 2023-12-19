package io.github.srcimon.screwbox.examples.platformer.map;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class MapGravity implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        final Vector gravity = Vector.of(
                map.properties().getDouble("gravity-x").orElse(0.0),
                map.properties().getDouble("gravity-y").orElse(700.0));
        return new Entity().add(new GravityComponent(gravity));
    }

}
