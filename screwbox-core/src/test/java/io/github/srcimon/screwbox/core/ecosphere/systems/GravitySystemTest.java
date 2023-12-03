package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.components.GravityComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.internal.DefaultEcosphere;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class GravitySystemTest {

    @Test
    void update_updatesEntitiesWithGravity(DefaultEcosphere entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(new PhysicsBodyComponent());
        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 10)));

        entities.addSystem(body, gravity);
        entities.addSystem(new GravitySystem());

        entities.update();

        Vector momentum = body.get(PhysicsBodyComponent.class).momentum;
        assertThat(momentum).isEqualTo(Vector.of(0, 5));
    }

}
