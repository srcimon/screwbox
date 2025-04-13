package io.github.srcimon.screwbox.platformer.map;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiles.Map;

public class MapBorderLeft implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(-200, 0, 200, map.bounds().height());
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
