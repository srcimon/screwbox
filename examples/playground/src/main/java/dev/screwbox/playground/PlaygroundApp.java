package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
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
                    final double padding = 10;
                    final double divisor = 220.0;
                    var z = e.loop().runningTime().milliseconds() / 400.0;
                    World world = e.graphics().world();
                    for (double y = area.minY(); y < area.maxY(); y += size + padding) {
                        for (double x = area.minX(); x < area.maxX(); x += size + padding) {
                            var noise = (PerlinNoise.generatePerlinNoise3D(123123L, x / divisor + 10000, y / divisor + 10000, z) + 1) / 2.0;

                            world.drawRectangle(Bounds.atOrigin(x, y, size * noise, size * noise),
                                    RectangleDrawOptions.filled(noise > 0.8 ? Color.WHITE : Color.BLUE));//TODO FIXME
                        }
                    }
                });

        engine.start();
    }
}