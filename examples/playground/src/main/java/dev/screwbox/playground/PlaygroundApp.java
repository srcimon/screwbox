package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.PerlinNoise;

public class PlaygroundApp {

    static double plus = 0;
    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.environment()
                .addSystem(new LogFpsSystem())
                .addSystem(e -> {
                    final Bounds area = e.graphics().visibleArea();
                    final double size = 10;
                    final double padding = 4;
                    final double divisor = 300.0;
                    var z = plus + e.loop().runningTime().milliseconds() / 8000.0;
                    plus += e.audio().microphoneLevel().rangeValue(0, 2) * e.loop().delta();
                    World world = e.graphics().world();
                    for (double y = area.minY(); y < area.maxY(); y += size + padding) {
                        for (double x = area.minX(); x < area.maxX(); x += size + padding) {
                            double x1 = x / divisor + 10000;
                            double y1 = y / divisor + 10000;
                            var noise = (PerlinNoise.generatePerlinNoise3D(123123L, x1, y1, z) + 1) / 2.0;
                            var noise2 = (PerlinNoise.generatePerlinNoise3D(112353L, x1, y1, z) + 1) / 2.0;

                            world.drawRectangle(Bounds.atOrigin(x, y, size * noise, size * noise),
                                    RectangleDrawOptions.filled(Color.rgb((int) (noise2 * 255), 0, (int) (noise * 255))));//TODO FIXME
                        }
                    }
                });

        engine.start();
    }
}