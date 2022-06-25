package de.suzufa.screwbox.core.entityengine.systems;

import static java.util.Objects.isNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.PathfindingBlockingComponent;
import de.suzufa.screwbox.core.entityengine.components.PathfindingNonBlockingComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.utils.Timer;

public class PathfindingGridCreationSystem implements EntitySystem {

    private static final Archetype BLOCKING = Archetype.of(
            PathfindingBlockingComponent.class, TransformComponent.class);

    private static final Archetype WORLD = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    private final int gridSize;
    private final Timer updateTimer;

    public PathfindingGridCreationSystem(final int gridSize, final Timer updateTimer) {
        this.gridSize = gridSize;
        this.updateTimer = updateTimer;
    }

    @Override
    public void update(final Engine engine) {
        if (needsUpdate(engine)) {
            final Bounds bounds = engine.entityEngine().forcedFetch(WORLD).get(TransformComponent.class).bounds;

            final Grid grid = new Grid(bounds, gridSize, true);
            for (final Entity blocking : engine.entityEngine().fetchAll(BLOCKING)) {
                if (!blocking.hasComponent(PathfindingNonBlockingComponent.class)) {
                    grid.blockArea(blocking.get(TransformComponent.class).bounds);
                }
            }
            engine.physics().updatePathfindingGrid(grid);
        }
    }

    private boolean needsUpdate(final Engine engine) {
        final Time time = engine.loop().metrics().timeOfLastUpdate();
        return isNull(engine.physics().pathfindingGrid()) || updateTimer.isTick(time);
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }

}
