package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class SuspendJumpControlSystemTest {

    JumpControlComponent jumpControlComponent;
    CollisionDetailsComponent collisionDetailsComponent;
    SuspendJumpControlComponent suspendJumpControlComponent;

    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        jumpControlComponent = new JumpControlComponent();
        collisionDetailsComponent = new CollisionDetailsComponent();
        suspendJumpControlComponent = new SuspendJumpControlComponent();

        environment.addEntity(new Entity()
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
        suspendJumpControlComponent.lastGroundDetection = Time.now().add(-10, Time.Unit.SECONDS);

        environment.update();

        assertThat(jumpControlComponent.isEnabled).isFalse();
    }
}
