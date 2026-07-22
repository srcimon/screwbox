package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.isNull;

/**
 * Applies diving to physics {@link Entity entities} that also contain {@link DiveComponent}.
 *
 * @since 3.1.0
 */
public class DiveSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Archetype DIVERS = Archetype.ofSpacial(DiveComponent.class, FloatComponent.class);

    @Override
    public void update(final Engine engine) {
        final var physicsEntities = engine.environment().fetchAll(PHYSICS);
        for (final var diver : engine.environment().fetchAll(DIVERS)) {
            final var floatComponent = diver.get(FloatComponent.class);
            final var diveComponent = diver.get(DiveComponent.class);

            if (isNull(diveComponent.inactiveDepth)) {
                diveComponent.inactiveDepth = floatComponent.dive;
            } else if (diveComponent.isDiving) {
                floatComponent.dive = diveComponent.inactiveDepth;
            } else {
                diveComponent.inactiveDepth = floatComponent.dive;
            }

            final var sensorBounds = Bounds.atOrigin(diver.origin().add(1, -0.5), diver.bounds().width() - 2, 1);
            for (final var physicsEntity : physicsEntities) {
                if (diver != physicsEntity && sensorBounds.touches(physicsEntity.bounds()) && !physicsEntity.get(PhysicsComponent.class).ignoreCollisions) {
                    diveComponent.isDiving = true;
                    floatComponent.dive = diveComponent.maxDepth;
                }
            }
        }
    }
}
