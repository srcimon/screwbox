package io.github.srcimon.screwbox.examples.platformer.map;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class MapBorderLeft implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(-200, 0, 200, map.bounds().height());
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
