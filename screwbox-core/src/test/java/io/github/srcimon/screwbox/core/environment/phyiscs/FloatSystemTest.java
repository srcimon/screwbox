package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.FloatComponent;
import io.github.srcimon.screwbox.core.environment.physics.FloatSystem;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.environment.physics.FluidSystem;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FloatSystemTest {

    @BeforeEach
    void setUp(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.02);

        environment
                .addSystem(new FluidSystem())
                .addSystem(new FloatSystem())
                .addEntity(new Entity().name("water")
                        .add(new FluidComponent(10))
                        .bounds($$(0, 0, 800, 400)));
    }

    @Test
    void update_entityIsAboveWater_doesntFloat(DefaultEnvironment environment) {
        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(0, -100, 40, 40)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNull();
        assertThat(physicsComponent.momentum).isEqualTo(Vector.zero());
    }

    @Test
    void update_entityIsUnderWater_floatsUp(DefaultEnvironment environment) {
        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();
        physicsComponent.momentum = $(10, 10);

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(100, 150, 40, 40)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNull();
        assertThat(physicsComponent.momentum).isEqualTo($(7, -4));
    }

    @Test
    void update_entityIsUnderWaterWithGravityEnabled_floatsUpAndRevertsGravityEffect(DefaultEnvironment environment) {
        environment.addEntity(new Entity().name("gravity").add(new GravityComponent(Vector.y(100))));

        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();
        physicsComponent.momentum = $(10, 10);

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(100, 150, 40, 40)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNull();
        assertThat(physicsComponent.momentum).isEqualTo($(7, -6));
    }
}
