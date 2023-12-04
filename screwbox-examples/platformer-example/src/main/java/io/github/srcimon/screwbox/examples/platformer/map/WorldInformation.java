package io.github.srcimon.screwbox.examples.platformer.map;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.examples.platformer.components.UseLightComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class WorldInformation implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new UseLightComponent(map.properties().getBoolean("uses-light").orElse(false)),
                new WorldBoundsComponent(),
                new TransformComponent(map.bounds()));
    }

}
