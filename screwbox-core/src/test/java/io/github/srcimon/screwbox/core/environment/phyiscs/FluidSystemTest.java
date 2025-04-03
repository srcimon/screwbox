package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.environment.physics.FluidSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FluidSystemTest {

    @BeforeEach
    void setUp(Loop loop) {
        when(loop.delta()).thenReturn(0.02);
    }

    @Test
    void update_noInteraction_noMovement(DefaultEnvironment environment) {
        FluidComponent fluid = new FluidComponent(10);

        environment
                .addEntity(new Entity().add(fluid))
                .addSystem(new FluidSystem());

        environment.update();

        assertThat(fluid.nodeCount).isEqualTo(10);
        assertThat(fluid.height).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(fluid.speed).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void update_waveExist_appliesSpeedAndHeightChange(DefaultEnvironment environment) {
        FluidComponent fluid = new FluidComponent(5);

        environment
                .addEntity(new Entity().add(fluid))
                .addSystem(new FluidSystem());

        fluid.speed[2] = 10;

        environment.update();

        assertThat(fluid.height).containsExactly(0, 0, 0.1, 0, 0);
        assertThat(fluid.speed).containsExactly(0, 0, 9.85, 0.03, 0);
    }

    @Test
    void update_waveExist_waveVanishesAfterSomeUpdates(DefaultEnvironment environment) {
        FluidComponent fluid = new FluidComponent(5);
        environment
                .addEntity(new Entity().add(fluid))
                .addSystem(new FluidSystem());

        fluid.speed[2] = 0.2;

        environment.updateTimes(200);

        assertThat(fluid.height[2]).isEqualTo(0, offset(0.01));
    }
}
