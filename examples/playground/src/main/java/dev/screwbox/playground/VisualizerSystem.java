package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.FractalNoise;

public class VisualizerSystem implements EntitySystem {

    private Vector offset = Vector.of(1000, 1000);

    @Override
    public void update(Engine engine) {
        offset = offset.add(engine.keyboard().arrowKeysMovement(2));
        final Bounds area = engine.graphics().visibleArea();
        final double size = 6;
        final double padding = 2;
        final double divisor = 4.0;
        var z = engine.loop().runningTime().milliseconds() / 50.0;
        World world = engine.graphics().world();

        for (double y = area.minY(); y < area.maxY(); y += size + padding) {
            for (double x = area.minX(); x < area.maxX(); x += size + padding) {
                final double x1 = x / divisor + offset.x();
                final double y1 = y / divisor + offset.y();
                final var noise = FractalNoise.generateFractalNoise3d(100.123123, 123123L, x1, y1, z).value();
                final var noise2 = FractalNoise.generateFractalNoise3d(100.123123, 12234233L, x1, y1, z).value();
                Color color = Color.DARK_BLUE;
                if (noise2 > 0.4 && noise2 < 0.5) {
                    color = Color.YELLOW;
                }
                if (noise2 < 0.4) {
                    color = Color.RED;
                }

                Bounds rectangle = Bounds.atOrigin(x + (noise - 0.5) * 100, y + (noise2 - 0.5) * 200, size, size);
                world.drawRectangle(rectangle, RectangleDrawOptions.filled(color));
            }
        }
    }
}
