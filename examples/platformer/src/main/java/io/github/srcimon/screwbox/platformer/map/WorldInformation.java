package io.github.srcimon.screwbox.platformer.map;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.platformer.components.UseLightComponent;
import dev.screwbox.tiles.Map;

public class WorldInformation implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new UseLightComponent(map.properties().tryGetBoolean("uses-light").orElse(false)),
                new CameraBoundsComponent(map.bounds().expand(-16)));
    }

}
