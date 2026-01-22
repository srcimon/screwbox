package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;

import static dev.screwbox.core.graphics.options.LineDrawOptions.color;
import static dev.screwbox.core.graphics.options.RectangleDrawOptions.filled;
import static java.util.Objects.nonNull;

@ExecutionOrder(Order.PRESENTATION_OVERLAY)
public class PhysicsDebugSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);
    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionSensorComponent.class);

    @Override
    public void update(final Engine engine) {
        final var world = engine.graphics().world();
        for (final var entity : engine.environment().fetchAll(COLLIDERS)) {
            renderEntity(world, entity, Color.BLUE);
        }

        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            renderEntity(world, entity, Color.RED);
        }
        for (final var entity : engine.environment().fetchAll(SENSORS)) {
            final var collisions = entity.get(CollisionSensorComponent.class).collidedEntities;
            for (final var collision : collisions) {
                renderEntity(world, collision, Color.GREEN);
            }
        }
    }

    private void renderEntity(final World world, final Entity entity, final Color color) {
        world.drawRectangle(entity.bounds(), filled(color.opacity(0.7)));

        final var physicsComponent = entity.get(PhysicsComponent.class);
        if (nonNull(physicsComponent)) {
            final Vector velocity = physicsComponent.velocity;
            final Vector destination = entity.position().add(velocity);

            world.drawLine(entity.position(), destination, color(Color.BLUE).strokeWidth(2));
        }
    }
}
