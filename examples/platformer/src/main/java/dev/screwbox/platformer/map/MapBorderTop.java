package dev.screwbox.platformer.map;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiled.Map;

public class MapBorderTop implements Blueprint<Map> {

    @Override
    public Entity assembleFrom(final Map map) {
        Bounds bounds = Bounds.atOrigin(0, -200, map.bounds().width(), 200);
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }
}
