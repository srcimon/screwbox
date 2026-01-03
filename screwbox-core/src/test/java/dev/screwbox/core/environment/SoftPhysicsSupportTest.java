package dev.screwbox.core.environment;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Vector.$;
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
    void createRope_idPool_throwsException() {
        assertThatThrownBy(() -> SoftPhysicsSupport.createRope($(20, 10), $(20, 10), 4, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("idPool must not be null");
    }
}
