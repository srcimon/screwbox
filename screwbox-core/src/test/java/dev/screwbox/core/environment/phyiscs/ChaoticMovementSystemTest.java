package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.ChaoticMovementSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ChaoticMovementSystemTest {

    @Test
    void update_changesPhysicsVelocity(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now().addSeconds(-2));
        when(loop.delta()).thenReturn(0.4);

        PhysicsComponent physics = new PhysicsComponent();

        environment.addEntity(physics, new ChaoticMovementComponent(20, Duration.ofSeconds(1)))
                .addSystem(new ChaoticMovementSystem());

        environment.update();

        assertThat(physics.velocity.x()).isNotZero().isBetween(-20.0, 20.0);
        assertThat(physics.velocity.y()).isNotZero().isBetween(-20.0, 20.0);
    }
}
