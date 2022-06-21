package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static de.suzufa.screwbox.core.Duration.ofMillis;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.utils.Timer;

public class PathfindingSystem {

    private Engine engine;
    private CollisionMap collisionMap;

    private Timer update = Timer.withInterval(ofMillis(500));

    public PathfindingSystem(Engine engine) {
        this.engine = engine;
        Entity worldBounds = engine.entityEngine().forcedFetch(WorldBoundsComponent.class, TransformComponent.class);
        Bounds bounds = worldBounds.get(TransformComponent.class).bounds;
        collisionMap = new CollisionMap(bounds, 16);

    }

    public void update() {
        if (update.isTick(engine.loop().metrics().timeOfLastUpdate())) {
            List<Bounds> nonWalkableBounds = new ArrayList<>();
            for (Entity entity : engine.entityEngine()
                    .fetchAll(Archetype.of(ColliderComponent.class, TransformComponent.class))) {
                if (!entity.hasComponent(PhysicsBodyComponent.class)) { // TODO: add special
                                                                        // NotBlockingPathfindingComponent
                    nonWalkableBounds.add(entity.get(TransformComponent.class).bounds);
                }
            }
            collisionMap.update(nonWalkableBounds);
        }
    }

    public void debugDraw() {
        collisionMap.debugDraw(engine.graphics().world());
    }

}
