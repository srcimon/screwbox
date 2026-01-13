package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;

@ExecutionOrder(PRESENTATION_WORLD)
public class RopeRenderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeRenderComponent.class, RopeComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            final var config = rope.get(RopeRenderComponent.class);
            final var shape = rope.get(RopeComponent.class).shape;

            engine.graphics().world().drawPolygon(shape, PolygonDrawOptions
                .outline(config.color)
                .strokeWidth(config.strokeWidth)
                .drawOrder(config.drawOrder)
                .smoothing(SPLINE));
        }
    }
}
