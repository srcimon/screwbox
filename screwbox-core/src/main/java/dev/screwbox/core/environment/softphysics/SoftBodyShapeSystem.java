package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;

import static java.util.Objects.isNull;

public class SoftBodyShapeSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.of(SoftBodyShapeComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            var softBody = body.get(SoftBodyComponent.class);
            var config = body.get(SoftBodyShapeComponent.class);
            if (isNull(config.shape)) {
                config.shape = softBody.shape;
            }
        }
    }
}
