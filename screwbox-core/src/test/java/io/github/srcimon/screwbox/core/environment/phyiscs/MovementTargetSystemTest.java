package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.MovementTargetComponent;
import io.github.srcimon.screwbox.core.environment.physics.MovementTargetSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class MovementTargetSystemTest {

    @Test
    void update_movesEntityTowardsTarget(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.2);
        environment
                .addSystem(new MovementTargetSystem())
                .addSystem(new PhysicsSystem())
                .addEntity(1, "mover",
                        new MovementTargetComponent($(100, 10)),
                        new TransformComponent(),
                        new PhysicsComponent());

        environment.updateTimes(10);

        var entity = environment.fetchById(1);
        assertThat(entity.position().x()).isEqualTo(100, offset(0.5));
        assertThat(entity.position().y()).isEqualTo(10, offset(0.5));
    }
}
