package de.suzufa.screwbox.playground.debo.map;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.resources.Converter;
import de.suzufa.screwbox.tiled.Map;

public class WorldBoundsConverter implements Converter<Map> {

    @Override
    public boolean accepts(Map map) {
        return true;
    }

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new WorldBoundsComponent(),
                new TransformComponent(map.bounds()));
    }

}
