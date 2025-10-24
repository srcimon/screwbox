package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class MotionRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, MotionRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            final var sprite = entity.get(RenderComponent.class);
            if (!physicsBody.velocity.isZero()) {
                Angle target = Angle.ofVector(physicsBody.velocity);
                Angle current = sprite.options.rotation();
                var change = current.delta(target);
                sprite.options = sprite.options.rotation(Angle.degrees(current.degrees() + change.degrees() *engine.loop().delta() * entity.get(MotionRotationComponent.class).maxSpeed));
            }
        }

    }

}
