package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.GravitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class GravitySystemTest {

    @Test
    void update_updatesEntitiesWithGravity(DefaultEnvironment environment, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(new PhysicsComponent());
        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 10)));

        environment.add(body, gravity);
        environment.addSystem(new GravitySystem());

        environment.update();

        Vector velocity = body.get(PhysicsComponent.class).velocity;
        assertThat(velocity).isEqualTo(Vector.of(0, 5));
    }

}
