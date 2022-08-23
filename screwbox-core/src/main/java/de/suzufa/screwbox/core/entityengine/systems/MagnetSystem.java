package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.MagnetComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class MagnetSystem implements EntitySystem {

    private static final Archetype MAGNETS = Archetype.of(TransformComponent.class, MagnetComponent.class);
    private static final Archetype BODIES = Archetype.of(TransformComponent.class, PhysicsBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        final var magnets = engine.entityEngine().fetchAll(MAGNETS);
        if (magnets.isEmpty()) {
            return;
        }
        final var bodies = engine.entityEngine().fetchAll(BODIES);
        double delta = engine.loop().metrics().delta();
        for (final var magnet : magnets) {
            Vector magnetPosition = magnet.get(TransformComponent.class).bounds.position();
            var magnetComponent = magnet.get(MagnetComponent.class);
            for (final var body : bodies) {
                if (!magnet.equals(body)) {
                    var bodyTransform = body.get(TransformComponent.class);
                    var bodyPhysics = body.get(PhysicsBodyComponent.class);
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
