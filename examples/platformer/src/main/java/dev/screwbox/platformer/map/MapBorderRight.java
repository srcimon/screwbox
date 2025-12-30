package dev.screwbox.platformer.map;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.tiled.Map;

public class MapBorderRight implements Blueprint<Map> {

    @Override
    public Entity assembleFrom(final Map map) {
        Bounds bounds = Bounds.atOrigin(map.bounds().width(), 0, 200, map.bounds().height());
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
