package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

public class MagnetSystem implements EntitySystem {

    private static final Archetype MAGNETS = Archetype.of(TransformComponent.class, MagnetComponent.class);
    private static final Archetype BODIES = Archetype.of(TransformComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var magnets = engine.environment().fetchAll(MAGNETS);
        if (magnets.isEmpty()) {
            return;
        }
        final var bodies = engine.environment().fetchAll(BODIES);
        double delta = engine.loop().delta();
        for (final var magnet : magnets) {
            Vector magnetPosition = magnet.get(TransformComponent.class).bounds.position();
            var magnetComponent = magnet.get(MagnetComponent.class);
            for (final var body : bodies) {
                if (!magnet.equals(body)) {
                    var bodyTransform = body.get(TransformComponent.class);
                    var bodyPhysics = body.get(PhysicsComponent.class);
                    var distance = bodyTransform.bounds.position().distanceTo(magnetPosition);
                    var force = Math.max(0, (magnetComponent.range - distance) / magnetComponent.range)
                            * magnetComponent.force * delta * bodyPhysics.magnetModifier;
                    bodyPhysics.momentum = bodyPhysics.momentum
                            .add(magnetPosition.substract(bodyTransform.bounds.position()).multiply(force));
                }
            }
        }
    }

}
