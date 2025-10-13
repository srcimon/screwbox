package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.navigation.Path;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;

import static dev.screwbox.core.graphics.Color.GREEN;
import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.LineDrawOptions.color;
import static dev.screwbox.core.graphics.options.RectangleDrawOptions.filled;
import static dev.screwbox.core.graphics.options.SystemTextDrawOptions.systemFont;
import static java.util.Objects.nonNull;

@Order(Order.SystemOrder.PRESENTATION_OVERLAY)
public class PathMovementDebugSystem implements EntitySystem {

    private static final SystemTextDrawOptions DRAW_OPTIONS = systemFont("Arial", 11).alignCenter().bold();
    private static final Archetype PATH_CONTAINING = Archetype.of(PathMovementComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(PATH_CONTAINING)) {
            Path path = entity.get(PathMovementComponent.class).path;
            if (nonNull(path)) {
                renderNearbyGridNodes(engine, path);
                renderPath(engine, path);
            }
        }
    }

    private void renderNearbyGridNodes(final Engine engine, final Path path) {
        final var world = engine.graphics().world();
        engine.navigation().grid().ifPresent(grid -> {
            for (var node : grid.nodes()) {
                final Bounds bounds = grid.worldArea(node);
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
                    .drawCircle(node, 1.5, OvalDrawOptions.filled(YELLOW));
        }
    }
}
