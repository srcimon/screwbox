package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.GravityComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.test.EntitiesExtension;

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
