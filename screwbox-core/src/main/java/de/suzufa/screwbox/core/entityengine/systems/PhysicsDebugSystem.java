package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.graphics.world.WorldLine.line;
import static de.suzufa.screwbox.core.graphics.world.WorldRectangle.rectangle;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.graphics.Color;

public class PhysicsDebugSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Archetype SENSORS = Archetype.of(CollisionSensorComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.entityEngine().fetchAll(COLLIDERS)) {
            renderEntity(engine, entity, Color.BLUE);
        }

        for (final var entity : engine.entityEngine().fetchAll(PHYSICS)) {
            renderEntity(engine, entity, Color.RED);
        }
        for (final var entity : engine.entityEngine().fetchAll(SENSORS)) {
            final var collisions = entity.get(CollisionSensorComponent.class).collidedEntities;
            for (final var collision : collisions) {
                renderEntity(engine, collision, Color.GREEN);
            }
        }
    }

    private void renderEntity(final Engine engine, final Entity entity, final Color color) {
        final var bounds = entity.get(TransformComponent.class).bounds;
        engine.graphics().world().draw(rectangle(bounds, color, Percentage.of(0.7)));

        if (entity.hasComponent(PhysicsBodyComponent.class)) {
            final Vector momentum = entity.get(PhysicsBodyComponent.class).momentum;
            final Vector destination = bounds.position().add(momentum);

            engine.graphics().world().draw(line(bounds.position(), destination, Color.BLUE));
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_OVERLAY;
    }

}
