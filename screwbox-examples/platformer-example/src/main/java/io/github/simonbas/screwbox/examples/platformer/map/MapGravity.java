package io.github.simonbas.screwbox.examples.platformer.map;

import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.GravityComponent;
import io.github.simonbas.screwbox.tiled.Map;

public class MapGravity implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        final Vector gravity = Vector.of(
                map.properties().getDouble("gravity-x").orElse(0.0),
                map.properties().getDouble("gravity-y").orElse(700.0));
        return new Entity().add(new GravityComponent(gravity));
    }

}
