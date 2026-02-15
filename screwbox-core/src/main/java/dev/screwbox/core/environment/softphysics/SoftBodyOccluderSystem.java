package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.PRESENTATION_PREPARE;

//TODO document
@ExecutionOrder(PRESENTATION_PREPARE)
public class SoftBodyOccluderSystem implements EntitySystem {

    private static final Archetype SOFT_BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftBodyOccluderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var softBody : engine.environment().fetchAll(SOFT_BODIES)) {
            final var shape = softBody.get(SoftBodyComponent.class).shape;
            final var occluderConfig = softBody.get(SoftBodyOccluderComponent.class);
            engine.graphics().light().addBackgdropOccluder(shape, occluderConfig.options);
        }
    }
}
