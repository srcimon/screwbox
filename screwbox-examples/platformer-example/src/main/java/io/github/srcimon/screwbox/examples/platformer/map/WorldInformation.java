package io.github.srcimon.screwbox.examples.platformer.map;

import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.SourceImport.Converter;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.WorldBoundsComponent;
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
