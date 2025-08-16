package dev.screwbox.core.environment.controls;

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
class LeftRightControlSystemTest {

    PhysicsComponent physicsComponent;
    LeftRightControlComponent leftRightControlComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        physicsComponent = new PhysicsComponent();
        leftRightControlComponent = new LeftRightControlComponent();

        environment.addEntity(new Entity()
                .add(physicsComponent)
                .add(leftRightControlComponent));

        environment.addSystem(new LeftRightControlSystem());
    }

    @Test
    void update_leftPressed_movesToTheLeft(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.isDown(DefaultControlSet.LEFT)).thenReturn(true);
        when(loop.delta(-600)).thenReturn(-12.2);

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(-12.2));

        environment.updateTimes(8);

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(-leftRightControlComponent.maxSpeed));
    }

    @Test
    void update_rightPressed_movesToTheRight(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.isDown(DefaultControlSet.RIGHT)).thenReturn(true);
        when(loop.delta(600)).thenReturn(12.2);

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(12.2));

        environment.updateTimes(8);

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(leftRightControlComponent.maxSpeed));
    }

    @Test
    void update_rightPressedButDisables_doesntMove(DefaultEnvironment environment, Keyboard keyboard) {
        when(keyboard.isDown(DefaultControlSet.RIGHT)).thenReturn(true);
        leftRightControlComponent.isEnabled = false;

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_nothingPressed_doesntMove(DefaultEnvironment environment) {
        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.zero());
    }

}
