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

    PhysicsComponent physicsComponent;
    JumpControlComponent jumpControlComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        physicsComponent = new PhysicsComponent();
        jumpControlComponent = new JumpControlComponent();

        environment.addEntity(new Entity()
                .add(physicsComponent)
                .add(jumpControlComponent));

        environment.addSystem(new JumpControlSystem());
    }

    @Test
    void update_jumpIsPressed_updatesMomentum(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.isPressed(DefaultControlSet.JUMP)).thenReturn(true);
        when(loop.time()).thenReturn(Time.atNanos(12381923));

        environment.update();
        assertThat(physicsComponent.momentum).isEqualTo(Vector.y(-200));
        assertThat(jumpControlComponent.lastActivation).isEqualTo(Time.atNanos(12381923));
    }

    @Test
    void update_jumpWasPressedWithinGracePeriod_updatesMomentum(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.isPressed(DefaultControlSet.JUMP)).thenReturn(true, false);
        when(loop.time()).thenReturn(Time.atNanos(12381923), Time.atNanos(12381923).addMillis(20));

        jumpControlComponent.isEnabled = false;
        environment.update();

        assertThat(physicsComponent.momentum).isEqualTo(Vector.zero());
        assertThat(jumpControlComponent.latestRequest).isEqualTo(Time.atNanos(12381923));

        jumpControlComponent.isEnabled = true;
        environment.update();
        assertThat(physicsComponent.momentum).isEqualTo(Vector.y(-200));
        assertThat(jumpControlComponent.lastActivation).isEqualTo(Time.atNanos(32381923));
    }

    @Test
    void update_jumpIsNotPressed_doesntChangeMomentum(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());
        environment.update();

        assertThat(physicsComponent.momentum).isEqualTo(Vector.zero());
        assertThat(jumpControlComponent.lastActivation.isUnset()).isTrue();
    }

    @Test
    void update_jumpIsPressedButDisabled_doesntChangeMomentum(DefaultEnvironment environment, Keyboard keyboard) {
        when(keyboard.isPressed(DefaultControlSet.JUMP)).thenReturn(true);
        jumpControlComponent.isEnabled = false;

        environment.update();

        assertThat(physicsComponent.momentum).isEqualTo(Vector.zero());
        assertThat(jumpControlComponent.lastActivation.isUnset()).isTrue();
    }
}
