package io.github.srcimon.screwbox.examples.platformer.map;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.SourceImport.Converter;
import io.github.srcimon.screwbox.core.ecosphere.components.ColliderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.tiled.Map;

public class CloseMapBottomConverter implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(0, map.bounds().height(), map.bounds().width(), 200);
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
