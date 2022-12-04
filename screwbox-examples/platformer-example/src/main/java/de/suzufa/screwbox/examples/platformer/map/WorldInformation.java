package de.suzufa.screwbox.examples.platformer.map;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.examples.platformer.components.UseLightComponent;
import de.suzufa.screwbox.tiled.Map;

public class WorldInformation implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new UseLightComponent(map.properties().getBoolean("uses-light").orElse(false)),
                new WorldBoundsComponent(),
                new TransformComponent(map.bounds()));
    }

}
