package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.GravityComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.internal.DefaultEntities;
import io.github.simonbas.screwbox.core.loop.Loop;
import io.github.simonbas.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class GravitySystemTest {

    @Test
    void update_updatesEntitiesWithGravity(DefaultEntities entities, Loop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(new PhysicsBodyComponent());
        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 10)));

        entities.add(body, gravity);
        entities.add(new GravitySystem());

        entities.update();

        Vector momentum = body.get(PhysicsBodyComponent.class).momentum;
        assertThat(momentum).isEqualTo(Vector.of(0, 5));
    }

}
