package io.github.srcimon.screwbox.core.environment.phyiscs;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.MagnetComponent;
import io.github.srcimon.screwbox.core.environment.physics.MagnetSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class MagnetSystemTest {

    @Test
    void update_magnetAndPhysicsPresent_addsMovementToPhysics(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.1);

        TransformComponent earthTransform = new TransformComponent(0, 100, 4, 4);

        environment
                .addSystem(new MagnetSystem())
                .addSystem(new PhysicsSystem())
                .addEntity("sun",
                        new TransformComponent(0, 0, 100, 100),
                        new MagnetComponent(10, 10000))
                .addEntity("earth",
                        earthTransform,
                        new PhysicsComponent());

        environment.updateTimes(4);

        assertThat(earthTransform.bounds.position().x()).isEqualTo(0);
        assertThat(earthTransform.bounds.position().y()).isEqualTo(45.3, offset(0.1));
    }

    @Test
    void update_noMagnetPresent_phyiscsIsNotAfected(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.1);

        TransformComponent earthTransform = new TransformComponent(0, 100, 4, 4);

        environment
                .addSystem(new MagnetSystem())
                .addSystem(new PhysicsSystem())
                .addEntity("earth",
                        earthTransform,
                        new PhysicsComponent());

        environment.updateTimes(4);

        assertThat(earthTransform.bounds.position()).isEqualTo(Vector.of(0, 100));
    }
}
