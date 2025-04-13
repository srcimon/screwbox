package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.environment.physics.FluidSystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;

@ExtendWith(EnvironmentExtension.class)
class FluidRenderSystemTest {


    @Test
    void update_multipleColors_drawsGradient(DefaultEnvironment environment, World world) {
        FluidComponent fluid = new FluidComponent(6);
        fluid.height[0] = 10;
        fluid.height[1] = -2;
        fluid.height[4] = 4;

        environment
                .addSystem(new FluidSystem())
                .addSystem(new FluidRenderSystem())
                .addEntity(new Entity()
                        .add(fluid)
                        .add(new FluidRenderComponent())
                        .bounds(Bounds.$$(24, 18, 100, 20)));

        environment.update();

        verify(world).drawPolygon(List.of($(24, 28), $(44, 16), $(64, 18), $(84, 18), $(104, 22), $(124, 18), $(124, 38), $(24, 38)),
                PolygonDrawOptions.verticalGradient(Color.hex("#777fd8").opacity(0.5), Color.hex("#3445ff").opacity(0.5)).smoothenHorizontally());
    }

    @Test
    void update_singleColor_drawsFilled(DefaultEnvironment environment, World world) {
        FluidComponent fluid = new FluidComponent(6);
        fluid.height[0] = 10;
        fluid.height[1] = -2;
        fluid.height[4] = 4;

        environment
                .addSystem(new FluidSystem())
                .addSystem(new FluidRenderSystem())
                .addEntity(new Entity()
                        .add(fluid)
                        .add(new FluidRenderComponent(Color.RED))
                        .bounds(Bounds.$$(24, 18, 100, 20)));

        environment.update();

        verify(world).drawPolygon(List.of($(24, 28), $(44, 16), $(64, 18), $(84, 18), $(104, 22), $(124, 18), $(124, 38), $(24, 38)),
                PolygonDrawOptions.filled(Color.RED).smoothenHorizontally());
    }
}