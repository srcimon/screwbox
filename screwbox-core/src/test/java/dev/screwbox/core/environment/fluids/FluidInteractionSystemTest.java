package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FluidInteractionSystemTest {

    @BeforeEach
    void setUp(Loop loop) {
        when(loop.delta()).thenReturn(0.5);
    }
    @Test
    void update_rockHitsWaterSpeedBelowThreshold_doesNotInteract(DefaultEnvironment environment) {

        FluidComponent fluid = new FluidComponent(40);

        environment.addSystem(new FluidInteractionSystem())
                .addEntity(new Entity().name("fluid")
                        .bounds(Bounds.atOrigin(0, 0, 800, 400))
                        .add(fluid))
                .addEntity(new Entity().name("stone")
                        .bounds(Bounds.atOrigin(40, -10, 40, 40))
                        .add(new FluidInteractionComponent(1, 20))
                        .add(new PhysicsComponent(Vector.y(-19))));

        environment.update();

        for (final var speed : fluid.speed) {
            assertThat(speed).isZero();
        }
    }

    @Test
    void update_rockHitsWaterSpeedAboveThreshold_doesNotInteract(DefaultEnvironment environment) {
        FluidComponent fluid = new FluidComponent(40);

        environment.addSystem(new FluidInteractionSystem())
                .addEntity(new Entity().name("fluid")
                        .bounds(Bounds.atOrigin(0, 0, 800, 400))
                        .add(fluid))
                .addEntity(new Entity().name("stone")
                        .bounds(Bounds.atOrigin(40, -10, 40, 40))
                        .add(new FluidInteractionComponent(1, 20))
                        .add(new PhysicsComponent(Vector.y(-80))));

        environment.update();

        assertThat(fluid.speed[0]).isZero();
        assertThat(fluid.speed[2]).isEqualTo(40.0);
        assertThat(fluid.speed[3]).isEqualTo(40.0);
        assertThat(fluid.speed[4]).isEqualTo(40.0);
        assertThat(fluid.speed[5]).isZero();
        assertThat(fluid.speed[39]).isZero();
    }
}
