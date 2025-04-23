package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import static java.util.Objects.isNull;

/**
 * Applies diving to physics {@link Entity entities} that also contain {@link DiveComponent}.
 *
 * @since 3.1.0
 */
public class DiveSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        final var physicsEntities = engine.environment().fetchAll(Archetype.ofSpacial(PhysicsComponent.class));
        for (var divinEntity : engine.environment().fetchAll(Archetype.ofSpacial(DiveComponent.class, FloatComponent.class))) {
            final var floatComponent = divinEntity.get(FloatComponent.class);
            final var diveComponent = divinEntity.get(DiveComponent.class);

            if (isNull(diveComponent.inactiveDepth)) {
                diveComponent.inactiveDepth = floatComponent.dive;
            } else if (diveComponent.isDiving) {
                floatComponent.dive = diveComponent.inactiveDepth;
            } else {
                diveComponent.inactiveDepth = floatComponent.dive;
            }

            final var sensorBounds = Bounds.atOrigin(divinEntity.origin().add(1, -0.5), divinEntity.bounds().width() - 2, 1);
            for (final var physicsEntity : physicsEntities) {
                if (divinEntity != physicsEntity && sensorBounds.touches(physicsEntity.bounds())) {
                    diveComponent.isDiving = true;
                    floatComponent.dive = diveComponent.maxDepth;
                }
            }
        }
    }
}
