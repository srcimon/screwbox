package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static de.suzufa.screwbox.core.Duration.ofMillis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.utils.Timer;

public class PathfindingSystem {

    private Engine engine;
    private Raster raster;

    private Timer update = Timer.withInterval(ofMillis(500));

    // TODO: merge with engine.physics()
    public PathfindingSystem(Engine engine) {
        this.engine = engine;
    }

    public Optional<Path> findPath(Vector start, Vector end) {
        RasterPoint startPoint = raster.getPoint(start);
        RasterPoint endPoint = raster.getPoint(end);
        var map = raster.createMap();
        List<RasterPoint> path = STOLENBACKUP.FindPath(map, startPoint, endPoint);
        if (path == null) {
            return Optional.empty();
        }

        List<Vector> list = path.stream().map(p -> raster.getVector(p)).toList();
        ArrayList<Vector> value = new ArrayList<>(list);
        value.add(end);
        return Optional.of(Path.withWaypoints(value));
    }

    public void update() {
        if (update.isTick(engine.loop().metrics().timeOfLastUpdate()) || raster == null) {
            Entity worldBounds = engine.entityEngine().forcedFetch(WorldBoundsComponent.class,
                    TransformComponent.class);
            Bounds bounds = worldBounds.get(TransformComponent.class).bounds;
            raster = new Raster(bounds, 16);
            for (Entity entity : engine.entityEngine()
                    .fetchAll(Archetype.of(ColliderComponent.class, TransformComponent.class))) {
                if (!entity.hasComponent(PhysicsBodyComponent.class)) { // TODO: add special
                    raster.blockArea(entity.get(TransformComponent.class).bounds);
                }
            }
        }
    }

}
