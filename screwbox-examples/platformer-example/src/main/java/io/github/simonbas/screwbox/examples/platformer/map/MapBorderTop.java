package io.github.simonbas.screwbox.examples.platformer.map;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.tiled.Map;

public class MapBorderTop implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(0, -200, map.bounds().width(), 200);
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
