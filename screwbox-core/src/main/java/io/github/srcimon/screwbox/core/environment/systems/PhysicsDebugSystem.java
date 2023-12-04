package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.components.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.environment.*;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class PhysicsDebugSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Archetype SENSORS = Archetype.of(CollisionSensorComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(COLLIDERS)) {
            renderEntity(engine, entity, Color.BLUE);
        }

        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            renderEntity(engine, entity, Color.RED);
        }
        for (final var entity : engine.environment().fetchAll(SENSORS)) {
            final var collisions = entity.get(CollisionSensorComponent.class).collidedEntities;
            for (final var collision : collisions) {
                renderEntity(engine, collision, Color.GREEN);
            }
        }
    }

    private void renderEntity(final Engine engine, final Entity entity, final Color color) {
        final var bounds = entity.get(TransformComponent.class).bounds;
        engine.graphics().world().fillRectangle(bounds, color.opacity(0.7));

        if (entity.hasComponent(PhysicsBodyComponent.class)) {
            final Vector momentum = entity.get(PhysicsBodyComponent.class).momentum;
            final Vector destination = bounds.position().add(momentum);

            engine.graphics().world().drawLine(bounds.position(), destination, Color.BLUE);
        }
    }
}
