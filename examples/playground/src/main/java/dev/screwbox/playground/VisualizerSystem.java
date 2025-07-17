package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.PerlinNoise;

public class VisualizerSystem implements EntitySystem {

    private double plus;

    @Override
    public void update(Engine engine) {
        final Bounds area = engine.graphics().visibleArea().expand(-96);
        final double size = 8;
        final double padding = 3;
        final double divisor = 300.0;
        plus += engine.audio().microphoneLevel().value()  * engine.loop().delta();
        var z = plus + engine.loop().runningTime().milliseconds() / 50000.0;
        World world = engine.graphics().world();

        for (double y = area.minY(); y < area.maxY(); y += size + padding) {
            for (double x = area.minX(); x < area.maxX(); x += size + padding) {
                double x1 = x / divisor + 10000;
                double y1 = y / divisor + 10000;
                var noise = (PerlinNoise.generatePerlinNoise3D(123123L, x1, y1, z) + 1) / 2.0;
                var noise2 = (PerlinNoise.generatePerlinNoise3D(112353L, x1, y1, z) + 1) / 2.0;

                    world.drawRectangle(Bounds.atOrigin(x + (noise - 0.5) * 100, y + (noise2 - 0.5) * 200, size * noise, size * noise),
                            RectangleDrawOptions.filled(Color.rgb((int) (noise2 * 255), 0, (int) (noise * 255))));//TODO FIXME
            }
        }
    }
}
