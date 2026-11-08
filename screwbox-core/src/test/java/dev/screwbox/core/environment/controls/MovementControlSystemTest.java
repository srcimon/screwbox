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
class MovementControlSystemTest {

    PhysicsComponent physicsComponent;
    MovementControlComponent movementControlComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        physicsComponent = new PhysicsComponent();
        movementControlComponent = new MovementControlComponent();

        environment.addEntity(new Entity()
            .add(physicsComponent)
            .add(movementControlComponent));

        environment.addSystem(new MovementControlSystem());
    }

    @Test
    void update_leftPressed_movesToTheLeft(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.axisSign(DefaultControlSet.LEFT, DefaultControlSet.RIGHT)).thenReturn(-1.0);
        when(loop.delta(-600)).thenReturn(-12.2);

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(-12.2));

        environment.updateTimes(8);

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(-movementControlComponent.maxSpeed));
    }

    @Test
    void update_notRestrictedToHorizontalMovementAndUpPressed_movesAlsoUp(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.axisSign(DefaultControlSet.LEFT, DefaultControlSet.RIGHT)).thenReturn(-1.0);
        when(keyboard.axisSign(DefaultControlSet.UP, DefaultControlSet.DOWN)).thenReturn(-1.0);
        when(loop.delta(-600)).thenReturn(-12.2);
        movementControlComponent.isHorizontalOnly = false;

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.$(-12.2, -12.2));

        environment.updateTimes(8);

        assertThat(physicsComponent.velocity).isEqualTo(Vector.$(-movementControlComponent.maxSpeed, -movementControlComponent.maxSpeed));
    }

    @Test
    void update_rightPressed_movesToTheRight(DefaultEnvironment environment, Keyboard keyboard, Loop loop) {
        when(keyboard.axisSign(DefaultControlSet.LEFT, DefaultControlSet.RIGHT)).thenReturn(1.0);
        when(loop.delta(600)).thenReturn(12.2);

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(12.2));

        environment.updateTimes(8);

        assertThat(physicsComponent.velocity).isEqualTo(Vector.x(movementControlComponent.maxSpeed));
    }

    @Test
    void update_rightPressedButDisables_doesntMove(DefaultEnvironment environment, Keyboard keyboard) {
        when(keyboard.axisSign(DefaultControlSet.LEFT, DefaultControlSet.RIGHT)).thenReturn(1.0);
        movementControlComponent.isEnabled = false;

        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.zero());
    }

    @Test
    void update_nothingPressed_doesntMove(DefaultEnvironment environment) {
        environment.update();

        assertThat(physicsComponent.velocity).isEqualTo(Vector.zero());
    }

}
