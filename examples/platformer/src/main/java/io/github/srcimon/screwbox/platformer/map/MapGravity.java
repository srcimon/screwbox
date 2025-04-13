package io.github.srcimon.screwbox.platformer.map;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.tiles.Map;

public class MapGravity implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        final Vector gravity = Vector.of(
                map.properties().tryGetDouble("gravity-x").orElse(0.0),
                map.properties().tryGetDouble("gravity-y").orElse(700.0));
        return new Entity().add(new GravityComponent(gravity));
    }

}
