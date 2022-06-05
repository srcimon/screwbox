package de.suzufa.screwbox.playground.debo.map;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.GravityComponent;
import de.suzufa.screwbox.core.resources.EntityConverter;
import de.suzufa.screwbox.tiled.Map;

public class MapGravity implements EntityConverter<Map> {

    @Override
    public boolean accepts(final Map map) {
        return true;
    }

    @Override
    public Entity convert(final Map map) {
        final Vector gravity = Vector.of(
                map.properties().getDouble("gravity-x").orElse(0.0),
                map.properties().getDouble("gravity-y").orElse(700.0));
        return new Entity().add(new GravityComponent(gravity));
    }

}
