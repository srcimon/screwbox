package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.keyboard.DefaultControlSet;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
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
        assertThat(jumpControlComponent.lastUnansweredRequest).isEqualTo(Time.atNanos(12381923));

        jumpControlComponent.isEnabled = true;
        environment.update();
        assertThat(physicsComponent.momentum).isEqualTo(Vector.y(-200));
        assertThat(jumpControlComponent.lastActivation).isEqualTo(Time.atNanos(32381923));
        assertThat(jumpControlComponent.lastUnansweredRequest).isEqualTo(Time.unset());
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
