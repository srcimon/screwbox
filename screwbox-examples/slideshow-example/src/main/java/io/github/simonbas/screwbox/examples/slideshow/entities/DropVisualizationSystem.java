package io.github.simonbas.screwbox.examples.slideshow.entities;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Color;

public class DropVisualizationSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.entities().fetchAllHaving(TransformComponent.class)) {
            var transform = entity.get(TransformComponent.class);
            transform.bounds = transform.bounds.inflated(engine.loop().delta() * 5);
            engine.graphics().world().drawFadingCircle(transform.bounds.position(), transform.bounds.width(), Color.RED);
        }
    }
}
