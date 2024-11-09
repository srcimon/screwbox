package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.FrictionSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FrictionSystemTest {

    @Test
    void update_entityHasNoFriction_doesntChangeMomentum(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        PhysicsComponent physics = new PhysicsComponent($(20, 20));

        environment.addEntity("spaceship", physics);
        environment.addSystem(new FrictionSystem());

        environment.update();

        assertThat(physics.momentum).isEqualTo($(20, 20));
    }

    @Test
    void update_entityHasNegativeFriction_entityIsSpeedUp(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        PhysicsComponent physics = new PhysicsComponent($(20, 20));
        physics.friction = -10;

        environment.addEntity("car on ice", physics);
        environment.addSystem(new FrictionSystem());

        environment.update();

        assertThat(physics.momentum.x()).isEqualTo(23.54, offset(0.01));
        assertThat(physics.momentum.y()).isEqualTo(23.54, offset(0.01));
    }

    @Test
    void update_updatesupdate_entityFriction_reducesMomentumUntilStopped(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        PhysicsComponent physics = new PhysicsComponent($(20, 20));
        physics.friction = 4;

        environment.addEntity("car", physics);
        environment.addSystem(new FrictionSystem());

        environment.update();

        assertThat(physics.momentum.x()).isEqualTo(18.59, offset(0.01));
        assertThat(physics.momentum.y()).isEqualTo(18.59, offset(0.01));

        environment.updateTimes(20);

        assertThat(physics.momentum).isEqualTo(Vector.zero());
    }
}
