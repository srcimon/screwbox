package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.LineDrawOptions.color;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class PhysicsDebugSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Archetype SENSORS = Archetype.of(CollisionDetectionComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(COLLIDERS)) {
            renderEntity(engine, entity, Color.BLUE);
        }

        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            renderEntity(engine, entity, Color.RED);
        }
        for (final var entity : engine.environment().fetchAll(SENSORS)) {
            final var collisions = entity.get(CollisionDetectionComponent.class).collidedEntities;
            for (final var collision : collisions) {
                renderEntity(engine, collision, Color.GREEN);
            }
        }
    }

    private void renderEntity(final Engine engine, final Entity entity, final Color color) {
        final var bounds = entity.get(TransformComponent.class).bounds;
        engine.graphics().world().drawRectangle(bounds, filled(color.opacity(0.7)));

        if (entity.hasComponent(PhysicsComponent.class)) {
            final Vector momentum = entity.get(PhysicsComponent.class).momentum;
            final Vector destination = bounds.position().add(momentum);

            engine.graphics().world().drawLine(bounds.position(), destination, color(Color.BLUE).strokeWidth(2));
        }
    }
}
