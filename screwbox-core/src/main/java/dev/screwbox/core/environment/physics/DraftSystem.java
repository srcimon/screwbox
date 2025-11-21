package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import java.util.List;

import static java.util.Objects.isNull;

public class DraftSystem implements EntitySystem {

    private static final Archetype SOURCES = Archetype.ofSpacial(DraftSourceComponent.class);
    private static final Archetype RECEIVERS = Archetype.ofSpacial(DraftReceiverComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var sources = engine.environment().fetchAll(SOURCES);
        if (sources.isEmpty()) {
            return;
        }
        final var physics = engine.environment().fetchAll(RECEIVERS);
        for (final var source : sources) {
            applyVelocityOnReceivers(physics, source, engine.loop().delta());
        }
    }

    private void applyVelocityOnReceivers(final List<Entity> receivers, final Entity effector, final double delta) {
        final var effect = effector.get(DraftSourceComponent.class);
        if (isNull(effect.lastPosition)) {
            effect.lastPosition = effector.position();
        }
        final var velocity = effector.position()
                .substract(effect.lastPosition)
                .multiply(1 / delta * effect.modifier.value());

        effect.lastPosition = effector.position();
        if (!velocity.isZero()) {
            final var interactionBounds = effector.bounds().expand(effect.range);
            for (final var receiver : receivers) {
                if (receiver.bounds().intersects(interactionBounds)) {
                    final var physicsComponent = receiver.get(PhysicsComponent.class);
                    if (physicsComponent.velocity.length() < velocity.length()) {
                        physicsComponent.velocity = physicsComponent.velocity.add(velocity.multiply(delta));
                    }
                }
            }
        }
    }
}
