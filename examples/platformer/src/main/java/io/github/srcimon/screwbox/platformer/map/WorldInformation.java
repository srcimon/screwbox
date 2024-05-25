package io.github.srcimon.screwbox.platformer.map;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.platformer.components.UseLightComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class WorldInformation implements Converter<Map> {

    @Override
    public Entity convert(Map map) {
        return new Entity().add(
                new UseLightComponent(map.properties().tryGetBoolean("uses-light").orElse(false)),
                new CameraBoundsComponent(map.bounds().expand(-16)));
    }

}
