package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ChaoticMovemenSystemTest {

    @Test
    void update_changesPhysicsMomentum(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now().addSeconds(-2));
        when(loop.delta()).thenReturn(0.4);

        PhysicsComponent physics = new PhysicsComponent();

        environment.addEntity(physics, new ChaoticMovementComponent(20, Duration.ofSeconds(1)))
                .addSystem(new ChaoticMovementSystem());

        environment.update();

        assertThat(physics.momentum.x()).isNotZero().isBetween(-20.0, 20.0);
        assertThat(physics.momentum.y()).isNotZero().isBetween(-20.0, 20.0);
    }
}
