package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ChaoticMovemenSystemTest {

    @Test
    void update_movesEntities(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now().addSeconds(-2));
        when(loop.delta()).thenReturn(0.4);


        environment.addEntity(1,
                        new PhysicsComponent(),
                        new TransformComponent($(410, 102)),
                        new ChaoticMovementComponent(20, Duration.ofSeconds(1)))
                .addSystem(new ChaoticMovementSystem());

        environment.update();

        assertThat(environment.fetchById(1).position()).isNotEqualTo($(410, 102));
    }
}
