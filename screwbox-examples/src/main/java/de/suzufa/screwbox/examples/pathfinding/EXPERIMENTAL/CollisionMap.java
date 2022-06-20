package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class CollisionMap {

    private boolean[][] map;
    private Vector origin;
    private int width;
    private int height;
    private int gridSize;

    public CollisionMap(Bounds worldBounds, int gridSize) {
        width = (int) worldBounds.width() / gridSize;
        height = (int) worldBounds.height() / gridSize;
        origin = worldBounds.origin();
        this.gridSize = gridSize;
        map = new boolean[width][height];
    }

    public void update(EntityEngine entityEngine) {
        Time now = Time.now();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = false;
            }
        }
        for (Entity entity : entityEngine.fetchAll(Archetype.of(ColliderComponent.class, TransformComponent.class))) {
            if (!entity.hasComponent(PhysicsBodyComponent.class)) {
                markArea(entity.get(TransformComponent.class).bounds);
            }
        }
        System.out.println(Duration.since(now).nanos());
    }

    private void markArea(Bounds bounds) {
        Vector boundsOrigin = bounds.origin();
        int xMin = (int) boundsOrigin.x() / gridSize;
        int yMin = (int) boundsOrigin.y() / gridSize;

        Vector bottomRight = bounds.bottomRight();
        int xMax = (int) bottomRight.x() / gridSize;
        int yMax = (int) bottomRight.y() / gridSize;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                map[x][y] = true;
            }
        }
    }
}
