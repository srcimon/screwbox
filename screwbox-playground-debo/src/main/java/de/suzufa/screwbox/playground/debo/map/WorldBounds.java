package de.suzufa.screwbox.playground.debo.map;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.tiled.Map;

public class WorldBounds implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new WorldBoundsComponent(),
                new TransformComponent(map.bounds()));
    }

}
