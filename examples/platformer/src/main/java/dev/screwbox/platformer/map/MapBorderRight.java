package dev.screwbox.platformer.map;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiles.Map;

public class MapBorderRight implements SourceImport.Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(map.bounds().width(), 0, 200, map.bounds().height());
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
