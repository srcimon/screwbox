package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.AirFrictionComponent;
import dev.screwbox.core.environment.physics.AirFrictionSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class AirFrictionSystemTest {

    @BeforeEach
    void setUp(Loop loop, DefaultEnvironment environment) {
        when(loop.delta()).thenReturn(0.21);
        Entity feather = new Entity()
                .add(new AirFrictionComponent(400))
                .add(new PhysicsComponent(Vector.$(400, 100)));

        environment.addEntity(feather)
                .addSystem(new AirFrictionSystem());
    }

    @Test
    void update_afterSomeUpdates_reducesSpeedToNothing(DefaultEnvironment environment) {
        environment.updateTimes(6);

        assertThat(environment.fetchSingletonComponent(PhysicsComponent.class).velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_onlyOneUpdate_reducesSpeed(DefaultEnvironment environment) {
        environment.update();

        assertThat(environment.fetchSingletonComponent(PhysicsComponent.class).velocity).isEqualTo(Vector.$(316.00, 16.00));
    }
}
