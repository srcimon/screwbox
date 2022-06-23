package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Objects.nonNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.World;

public class AutomovementDebugSystem implements EntitySystem {

    private static final Font FONT = new Font("Arial", 11);
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
                int nr = 0;
                for (var node : path.nodes()) {
                    nr++;
                    world.drawTextCentered(node.addY(-5), "" + nr, FONT, Color.WHITE)
                            .drawRectangle(Bounds.atPosition(node, 2, 2), Color.YELLOW);
                    // TODO: drawcircle
                }
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_OVERLAY;
    }
}
