package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.Polygon;

import java.util.Optional;

import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.LineDrawOptions.color;
import static dev.screwbox.core.graphics.options.SystemTextDrawOptions.systemFont;

@ExecutionOrder(Order.PRESENTATION_OVERLAY)
public class PathMovementDebugSystem implements EntitySystem {

    private static final LineDrawOptions LINE_OPTIONS = color(YELLOW).strokeWidth(2);
    private static final SystemTextDrawOptions DRAW_OPTIONS = systemFont("Arial", 11).alignCenter().bold();
    private static final Archetype PATHS = Archetype.of(PathMovementComponent.class);

    @Override
    public void update(Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(PATHS)) {
            Optional.ofNullable(entity.get(PathMovementComponent.class).path).ifPresent(path ->
                    renderPath(engine, path));
        }
    }

    private void renderPath(final Engine engine, final Polygon path) {
        final var world = engine.graphics().world();
        for (var segment : path.segments()) {
            world.drawLine(segment, LINE_OPTIONS);
        }
        int nr = 0;
        for (var node : path.definitionNotes()) {
            nr++;
            world.drawText(node.addY(-5), "#" + nr, DRAW_OPTIONS)
                    .drawCircle(node, 1.5, OvalDrawOptions.filled(YELLOW));
        }
    }
}
