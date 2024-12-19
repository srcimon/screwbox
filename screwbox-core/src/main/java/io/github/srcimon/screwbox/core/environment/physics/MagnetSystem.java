package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class MagnetSystem implements EntitySystem {

    private static final Archetype MAGNETS = Archetype.ofSpacial(MagnetComponent.class);
    private static final Archetype BODIES = Archetype.ofSpacial(PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var magnets = engine.environment().fetchAll(MAGNETS);
        if (magnets.isEmpty()) {
            return;
        }
        final var bodies = engine.environment().fetchAll(BODIES);
        final var delta = engine.loop().delta();
        for (final var magnet : magnets) {
            final Vector magnetPosition = magnet.position();
            final var magnetComponent = magnet.get(MagnetComponent.class);
            for (final var body : bodies) {
                if (!magnet.equals(body)) {
                    var bodyPhysics = body.get(PhysicsComponent.class);
                    var distance = body.position().distanceTo(magnetPosition);
                    var force = Math.max(0, (magnetComponent.range - distance) / magnetComponent.range)
                            * magnetComponent.force * delta * bodyPhysics.magnetModifier;
                    bodyPhysics.momentum = bodyPhysics.momentum
                            .add(magnetPosition.substract(body.position()).multiply(force));
                }
            }
        }
    }
}
