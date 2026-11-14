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
    void update_targetPositionSpecified_movesEntityTowardsTargetPosition(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.05);
        environment
            .addSystem(new TargetMovementSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(1, "mover",
                new TargetMovementComponent($(1000, 10)),
                new TransformComponent(),
                new PhysicsComponent());

        environment.updateTimes(10);

        var entity = environment.fetchById(1);
        assertThat(entity.position().x()).isEqualTo(42.49, offset(0.5));
        assertThat(entity.position().y()).isEqualTo(0.43, offset(0.5));
    }

    @Test
    void update_targetId_movesEntityTowardsTargetEntity(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.2);
        environment
            .addSystem(new TargetMovementSystem())
            .addSystem(new PhysicsSystem())
            .addEntity(124, "target", new TransformComponent($(1000, 10)))
            .addEntity(1, "mover",
                new TargetMovementComponent(124),
                new TransformComponent(),
                new PhysicsComponent());

        environment.updateTimes(10);

        var entity = environment.fetchById(1);
        assertThat(entity.get(TargetMovementComponent.class).position).isEqualTo($(1000, 10));
        assertThat(entity.position().x()).isEqualTo(180, offset(0.5));
        assertThat(entity.position().y()).isEqualTo(1.8, offset(0.5));
        assertThat(entity.get(PhysicsComponent.class).velocity.length()).isEqualTo(100.0, offset(0.1));
    }
}
