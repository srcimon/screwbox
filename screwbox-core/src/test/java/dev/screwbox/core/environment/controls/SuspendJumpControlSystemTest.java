package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Line;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SuspendJumpControlSystemTest {

    JumpControlComponent jumpControlComponent;
    CollisionDetailsComponent collisionDetailsComponent;
    SuspendJumpControlComponent suspendJumpControlComponent;
    FloatComponent floatComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        jumpControlComponent = new JumpControlComponent();
        collisionDetailsComponent = new CollisionDetailsComponent();
        suspendJumpControlComponent = new SuspendJumpControlComponent();
        floatComponent = new FloatComponent();

        environment.addEntity(new Entity()
                .add(floatComponent)
                .add(suspendJumpControlComponent)
                .add(collisionDetailsComponent)
                .add(jumpControlComponent));

        environment.addSystem(new SuspendJumpControlSystem());
    }


    @Test
    void update_hadRecentGroundContact_leavesControlEnabled(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        collisionDetailsComponent.lastBottomContact = Time.now();

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isTrue();
    }

    @Test
    void update_lastBottomContactUnset_disablesJumpControl(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isFalse();
    }

    @Test
    void update_noRecentGroundContact_disablesJumpControl(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        collisionDetailsComponent.lastBottomContact = Time.now().add(-10, Time.Unit.SECONDS);

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isFalse();
    }

    @Test
    void update_isFloating_leavesControlEnabled(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        floatComponent.attachedWave = Line.between($(1, 2), $(2, 3));
        collisionDetailsComponent.lastBottomContact = Time.now().add(-10, Time.Unit.SECONDS);

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isTrue();
    }

    @Test
    void update_isFloatingButNoJumpWhenFloating_disablesJumpControl(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        suspendJumpControlComponent.allowJumpWhileFloating = false;
        floatComponent.attachedWave = Line.between($(1, 2), $(2, 3));
        collisionDetailsComponent.lastBottomContact = Time.now().add(-10, Time.Unit.SECONDS);

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isFalse();
    }
}
