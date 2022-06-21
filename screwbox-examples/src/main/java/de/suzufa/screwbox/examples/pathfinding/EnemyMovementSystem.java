package de.suzufa.screwbox.examples.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL.CollisionMap;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype ENEMIES = Archetype.of(
            EnemyMovementComponent.class, PhysicsBodyComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldBounds = engine.entityEngine().forcedFetch(WorldBoundsComponent.class, TransformComponent.class);
        Bounds bounds = worldBounds.get(TransformComponent.class).bounds;

        CollisionMap collisionMap = new CollisionMap(bounds, 16);
        List<Bounds> nonWalkableBounds = new ArrayList<>();
        for (Entity entity : engine.entityEngine()
                .fetchAll(Archetype.of(ColliderComponent.class, TransformComponent.class))) {
            if (!entity.hasComponent(PhysicsBodyComponent.class)) { // TODO: add special NotBlockingPathfindingComponent
                nonWalkableBounds.add(entity.get(TransformComponent.class).bounds);
            }
        }
        collisionMap.update(nonWalkableBounds);
        collisionMap.debugDraw(engine.graphics().world());
        Entity player = engine.entityEngine().forcedFetch(PlayerMovementComponent.class, TransformComponent.class);
        Vector playerPosition = player.get(TransformComponent.class).bounds.position();

        for (Entity enemy : engine.entityEngine().fetchAll(ENEMIES)) {
            Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI;// TODO: fix
    }

}
