package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Objects.nonNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.World;

public class AutomovementDebugSystem implements EntitySystem {

    private static final Archetype PATH_CONTAINING = Archetype.of(AutomovementComponent.class);

    @Override
    public void update(Engine engine) {
        World world = engine.graphics().world();
        for (Entity entity : engine.entityEngine().fetchAll(PATH_CONTAINING)) {
            Path path = entity.get(AutomovementComponent.class).path;
            if (nonNull(path)) {
                for (var segment : path.segments()) {
                    world.drawLine(segment, Color.YELLOW);
                }
                for (var waypoint : path.waypoints()) {
                    // TODO: drawcircle
                    world.drawRectangle(Bounds.atPosition(waypoint, 2, 2), Color.YELLOW);
                }
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_OVERLAY;
    }
}
