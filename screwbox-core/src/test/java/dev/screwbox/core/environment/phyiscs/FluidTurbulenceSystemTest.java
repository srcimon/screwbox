package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.FluidComponent;
import dev.screwbox.core.environment.physics.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.FluidTurbulenceSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FluidTurbulenceSystemTest {

    @Test
    void update_addsNoiseToAllNodes(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        FluidComponent fluid = new FluidComponent(4);

        environment
                .addSystem(new FluidTurbulenceSystem())
                .addEntity(new Entity()
                        .add(fluid)
                        .add(new FluidTurbulenceComponent()));

        environment.update();

        assertThat(fluid.speed).doesNotContain(0, 0, 0, 0);

        for (double speed : fluid.speed) {
            assertThat(speed).isBetween(-100.0, 100.0);
        }
    }

}