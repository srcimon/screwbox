package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.options.ShadowOptions;

import static dev.screwbox.core.environment.Order.PRESENTATION_PREPARE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

//TODO document
@ExecutionOrder(PRESENTATION_PREPARE)
public class SoftPhysicsOccluderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, RopeOccluderComponent.class);
    private static final Archetype SOFT_BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftBodyOccluderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            final var shape = rope.get(RopeComponent.class).shape;
            final var occluderConfig = rope.get(RopeOccluderComponent.class);
            final var renderConfig = rope.get(RopeRenderComponent.class);
            final var shadowPolygon = shape.stroked(isNull(renderConfig)
                ? occluderConfig.minStrokeWidth
                : Math.max(renderConfig.strokeWidth, occluderConfig.minStrokeWidth));
            engine.graphics().light().addBackgdropOccluder(shadowPolygon, occluderConfig.options);
        }

        for (final var softBody : engine.environment().fetchAll(SOFT_BODIES)) {
            final var shape = softBody.get(SoftBodyComponent.class).shape;
            final var occluderConfig = softBody.get(SoftBodyOccluderComponent.class);
            engine.graphics().light().addBackgdropOccluder(shape, occluderConfig.options);
        }
    }
}
