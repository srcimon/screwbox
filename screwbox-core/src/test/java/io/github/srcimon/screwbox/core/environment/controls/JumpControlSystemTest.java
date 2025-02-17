package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.DefaultControlSet;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class JumpControlSystemTest {

    Entity entity;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        entity = new Entity()
                .add(new PhysicsComponent())
                .add(new JumpControlComponent());

        environment.addEntity(entity);
        environment.addSystem(new JumpControlSystem());
    }

    @Test
    void update_jumpIsPressed_updatesMomentum(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.isPressed(DefaultControlSet.JUMP)).thenReturn(true);
        when(loop.time()).thenReturn(Time.atNanos(12381923));

        environment.update();
        assertThat(entity.get(PhysicsComponent.class).momentum).isEqualTo(Vector.y(-100));
        assertThat(entity.get(JumpControlComponent.class).lastActivation).isEqualTo(Time.atNanos(12381923));
    }

    @Test
    void update_jumpIsNotPressed_doesntChangeMomentum(DefaultEnvironment environment) {
        environment.update();

        assertThat(entity.get(PhysicsComponent.class).momentum).isEqualTo(Vector.zero());
        assertThat(entity.get(JumpControlComponent.class).lastActivation.isUnset()).isTrue();
    }
}
