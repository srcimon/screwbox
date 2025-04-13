package dev.screwbox.core.environment.ai;

import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsSystem;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TargetMovementSystemTest {

    @Test
    void update_movesEntityTowardsTarget(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.2);
        environment
                .addSystem(new TargetMovementSystem())
                .addSystem(new PhysicsSystem())
                .addEntity(1, "mover",
                        new TargetMovementComponent($(100, 10)),
                        new TransformComponent(),
                        new PhysicsComponent());

        environment.updateTimes(10);

        var entity = environment.fetchById(1);
        assertThat(entity.position().x()).isEqualTo(100, offset(0.5));
        assertThat(entity.position().y()).isEqualTo(10, offset(0.5));
    }
}
