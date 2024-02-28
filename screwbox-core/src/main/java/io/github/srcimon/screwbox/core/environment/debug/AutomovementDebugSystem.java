package io.github.srcimon.screwbox.core.environment.debug;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementComponent;
import io.github.srcimon.screwbox.core.graphics.*;

import static java.util.Objects.nonNull;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class AutomovementDebugSystem implements EntitySystem {

    private static final Font FONT = new Font("Arial", 11);
    private static final Archetype PATH_CONTAINING = Archetype.of(AutomovementComponent.class);

    @Override
    public void update(Engine engine) {
        World world = engine.graphics().world();


        for (Entity entity : engine.environment().fetchAll(PATH_CONTAINING)) {
            Path path = entity.get(AutomovementComponent.class).path;

            if (nonNull(path)) {
                engine.physics().grid().ifPresent(grid -> {
                    for (var node : grid.nodes()) {
                        Bounds bounds = grid.worldArea(node);
                        if (bounds.position().nearestOf(path.nodes()).distanceTo(bounds.position()) < grid.gridSize() * 2) {
                            engine.graphics().world().drawRectangle(bounds, RectangleDrawOptions.filled(grid.isBlocked(node) ? Color.RED.opacity(0.5) : Color.GREEN.opacity(0.5)));
                        }
                    }
                });
                for (var segment : path.segments()) {
                    world.drawLine(segment, LineDrawOptions.color(Color.YELLOW).strokeWidth(2));
                }
                int nr = 0;
                for (var node : path.nodes()) {
                    nr++;
                    world.drawTextCentered(node.addY(-5), "#" + nr, FONT, Color.WHITE)
                            .fillCircle(node, 3, Color.YELLOW);
                }
            }
        }

    }
}
