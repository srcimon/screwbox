package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EnvironmentExtension.class)
class JumpControlSystemTest {

    @Test
    void xxxx(DefaultEnvironment environment) {
        environment.addEntity(new Entity().add(new PhysicsComponent()).add(new JumpControlComponent()))
    }
}
