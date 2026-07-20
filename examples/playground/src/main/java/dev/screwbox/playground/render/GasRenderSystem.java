package dev.screwbox.playground.render;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.playground.simulate.GasSimulationComponent;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class GasRenderSystem implements EntitySystem {

    //TODO Optimization: reuse image
    private static final Archetype GASSES = Archetype.ofSpacial(GasSimulationComponent.class, GasRenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var gas : engine.environment().fetchAll(GASSES)) {
            var simulation = gas.get(GasSimulationComponent.class);
            if (nonNull(simulation.state)) {
                BufferedImage image = new BufferedImage(simulation.state.size().width(), simulation.state.size().height(), BufferedImage.TYPE_INT_RGB);
                var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

                Arrays.parallelSetAll(pixels, gid -> {
                    int x = gid % simulation.state.size().width();
                    int y = gid / simulation.state.size().width();

                    int r = (int) (Math.clamp(simulation.state.get(x, y).density, 0, 1.0) * 255);

                    return (0xFF << 24) | (r << 16);
                });
                ImageOperations.blurImage(image, 3);
                engine.graphics().world().drawSprite(Sprite.fromImage(image), gas.origin(), SpriteDrawOptions.scaled(simulation.cellSize));
            }
        }
    }
}
