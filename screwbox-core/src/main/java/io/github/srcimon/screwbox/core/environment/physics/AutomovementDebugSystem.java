package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.Color.GREEN;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;
import static io.github.srcimon.screwbox.core.graphics.LineDrawOptions.color;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;
import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;
import static java.util.Objects.nonNull;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class AutomovementDebugSystem implements EntitySystem {

    private static final SystemTextDrawOptions DRAW_OPTIONS = systemFont("Arial", 11).alignCenter().bold();
    private static final Archetype PATH_CONTAINING = Archetype.of(AutomovementComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(PATH_CONTAINING)) {
            Path path = entity.get(AutomovementComponent.class).path;
            if (nonNull(path)) {
                renderNearbyGridNodes(engine, path);
                renderPath(engine, path);
            }
        }
    }

    private void renderNearbyGridNodes(final Engine engine, final Path path) {
        final var world = engine.graphics().world();
        engine.physics().grid().ifPresent(grid -> {
            for (var node : grid.nodes()) {
                Bounds bounds = grid.worldArea(node);
                if (bounds.position().nearestOf(path.nodes()).distanceTo(bounds.position()) < grid.gridSize() * 2) {
                    world.drawRectangle(bounds, filled(grid.isBlocked(node) ? RED.opacity(0.5) : GREEN.opacity(0.5)));
                }
            }
        });
    }

    private void renderPath(final Engine engine, final Path path) {
        final var world = engine.graphics().world();
        for (var segment : path.segments()) {
            world.drawLine(segment, color(YELLOW).strokeWidth(2));
        }
        int nr = 0;
        for (var node : path.nodes()) {
            nr++;
            world.drawText(node.addY(-5), "#" + nr, DRAW_OPTIONS)
                    .drawCircle(node, 1.5, CircleDrawOptions.filled(YELLOW));
        }
    }
}
