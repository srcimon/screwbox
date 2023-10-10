package io.github.srcimon.screwbox.examples.animation.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.examples.animation.components.MeanderAroundPositionComponent;

public class MeanderAroundPositionSystem implements EntitySystem {

    private static final Archetype MEANDERING = Archetype.of(MeanderAroundPositionComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final Time time = engine.loop().lastUpdate();

        for (var entity : engine.entities().fetchAll(MEANDERING)) {
            final var transform = entity.get(TransformComponent.class);
            final var meander = entity.get(MeanderAroundPositionComponent.class);

            var meanderdPosition = meander.origin
                    .addX(meander.x.value(time) * meander.maxDistance)
                    .addY(meander.y.value(time) * meander.maxDistance);

            transform.bounds = transform.bounds.moveTo(meanderdPosition);
        }
    }
}
