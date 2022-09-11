package de.suzufa.screwbox.playground.debo.map;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.tiled.Map;

public class CloseMapBottomConverter implements Converter<Map> {

    @Override
    public Entity convert(final Map map) {
        Bounds bounds = Bounds.atOrigin(0, map.bounds().height(), map.bounds().width(), 200);
        return new Entity().add(
                new TransformComponent(bounds),
                new ColliderComponent(500));
    }

}
