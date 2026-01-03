package dev.screwbox.core.environment;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(EnvironmentExtension.class)
class SoftPhysicsSupportTest {

    @Test
    void createRope_startNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope(null, $(20, 10), 4, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("start must not be null");
    }

    @Test
    void createRope_endNull_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope($(20, 10), null, 4, environment))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("end must not be null");
    }

    @Test
    void createRope_twoNodes_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope($(20, 10), $(40, 10), 2, environment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("nodeCount must be between 3 and 4096 (actual value: 2)");
    }

    @Test
    void createRope_startEqualsEnd_throwsException(DefaultEnvironment environment) {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope($(20, 10), $(20, 10), 4, environment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("rope start should be different from end");
    }

    @Test
    void createRope_idPool_throwsException() {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope($(20, 10), $(40, 10), 4, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("idPool must not be null");
    }

    @Test
    void xxxxx(DefaultEnvironment environment) {
        List<Entity> rope = SoftPhysicsSupport.createRope($(20, 10), $(30, 10), 8, environment);

        assertThat(rope).hasSize(8);
        assertThat(rope).allMatch(node -> node.hasComponent(PhysicsComponent.class));

        assertThat(rope.getFirst().position()).isEqualTo($(20, 10));
        assertThat(rope.getFirst().hasComponent(RopeComponent.class)).isTrue();
        assertThat(rope.getFirst().get(SoftLinkComponent.class).targetId).isEqualTo(rope.get(1).forceId());

        assertThat(rope.getLast().hasComponent(SoftLinkComponent.class)).isFalse();
        assertThat(rope.getLast().position()).isEqualTo($(30, 10));
    }
}
