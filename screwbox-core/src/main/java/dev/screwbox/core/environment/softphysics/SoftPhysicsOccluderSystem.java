package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.PRESENTATION_PREPARE;

//TODO document
@ExecutionOrder(PRESENTATION_PREPARE)
public class SoftPhysicsOccluderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeComponent.class, RopeOccluderComponent.class, RopeRenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            final var shape = rope.get(RopeComponent.class).shape;
            final var renderConfig = rope.get(RopeRenderComponent.class);
            final var occluderConfig = rope.get(RopeOccluderComponent.class);
            final var shadowPolygon = shape.stroked(renderConfig.strokeWidth * occluderConfig.strokeWidthModifier);
            engine.graphics().light().addBackgdropOccluder(shadowPolygon, occluderConfig.options);
        }
    }
}
