package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.PerlinNoise;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.environment()
                .addSystem(new LogFpsSystem())
                .addSystem(e -> {
                    final Bounds area = e.graphics().visibleArea();
                    final double size = 10;
                    final double padding = 0;
                    final double divisor = 220.0;
                    var z = e.loop().runningTime().milliseconds() / 200.0;
                    for (double y = area.minY(); y < area.maxY(); y += size + padding) {
                        for (double x = area.minX(); x < area.maxX(); x += size + padding) {
                            var noise = (PerlinNoise.generatePerlinNoise3D(123123L, x / divisor + 10000, y / divisor + 10000, z) + 1) / 2.0;
                            Color rgb = Color.rgb(0, 0, Math.clamp((int) (noise * 500)-200, 0, 255));
                            e.graphics().world().drawRectangle(Bounds.atOrigin(x, y, size, size),
                                    RectangleDrawOptions.filled(rgb));//TODO FIXME
                        }
                    }
                });

        engine.start();
    }
}