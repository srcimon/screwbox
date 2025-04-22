package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.isNull;

public class SubmergeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        final var physicsEntities = engine.environment().fetchAll(Archetype.ofSpacial(PhysicsComponent.class));
        for (var submergeEntity : engine.environment().fetchAll(Archetype.ofSpacial(SubmergeComponent.class, FloatComponent.class))) {
            final var floatComponent = submergeEntity.get(FloatComponent.class);
            final var submergeComponent = submergeEntity.get(SubmergeComponent.class);
            final var sensorBounds = Bounds.atOrigin(submergeEntity.origin().add(1, -0.5), submergeEntity.bounds().width() - 2, 1);
            if (isNull(submergeComponent.normalDepth)) {
                submergeComponent.normalDepth = floatComponent.submerge;
            } else {
                floatComponent.submerge = submergeComponent.normalDepth;
            }
            for (var physicsEntity : physicsEntities) {
                if (submergeEntity != physicsEntity && sensorBounds.touches(physicsEntity.bounds())) {
                    floatComponent.submerge = submergeComponent.depth;
                }
            }
        }
    }
}
