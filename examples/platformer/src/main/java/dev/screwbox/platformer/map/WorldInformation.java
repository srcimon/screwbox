package dev.screwbox.platformer.map;

import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.platformer.components.UseLightComponent;
import dev.screwbox.tiled.Map;

public class WorldInformation implements Blueprint<Map> {

    @Override
    public Entity assembleFrom(Map map) {
        return new Entity()
                .bounds(map.bounds().expand(-16))
                .add(new UseLightComponent(map.properties().tryGetBoolean("uses-light").orElse(false)))
                .add(new CameraBoundsComponent());
    }

}
