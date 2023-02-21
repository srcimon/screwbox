package io.github.simonbas.screwbox.examples.platformer.map;

import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.components.WorldBoundsComponent;
import io.github.simonbas.screwbox.examples.platformer.components.UseLightComponent;
import io.github.simonbas.screwbox.tiled.Map;

public class WorldInformation implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new UseLightComponent(map.properties().getBoolean("uses-light").orElse(false)),
                new WorldBoundsComponent(),
                new TransformComponent(map.bounds()));
    }

}
