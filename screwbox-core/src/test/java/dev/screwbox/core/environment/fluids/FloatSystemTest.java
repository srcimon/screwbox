package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
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
    void update_entityIsNearSurface_attachesWave(DefaultEnvironment environment) {
        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(795, -20, 10, 80)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNotNull();
        assertThat(floatComponent.attachedWave.end()).isEqualTo($(800, 0));
        assertThat(physicsComponent.velocity).isEqualTo($(0, -7));
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
        assertThat(physicsComponent.velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_entityIsUnderWater_floatsUp(DefaultEnvironment environment) {
        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();
        physicsComponent.velocity = $(10, 10);

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(100, 150, 40, 40)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNull();
        assertThat(physicsComponent.velocity).isEqualTo($(7, -3));
    }

    @Test
    void update_entityIsUnderWaterWithGravityEnabled_floatsUpAndRevertsGravityEffect(DefaultEnvironment environment) {
        environment.addEntity(new Entity().name("gravity").add(new GravityComponent(Vector.y(100))));

        var floatComponent = new FloatComponent();
        var physicsComponent = new PhysicsComponent();
        physicsComponent.velocity = $(10, 10);

        environment.addEntity(new Entity().name("boat")
                .add(physicsComponent)
                .add(floatComponent)
                .bounds($$(100, 150, 40, 40)));

        environment.update();

        assertThat(floatComponent.attachedWave).isNull();
        assertThat(physicsComponent.velocity).isEqualTo($(7, -5));
    }
}
