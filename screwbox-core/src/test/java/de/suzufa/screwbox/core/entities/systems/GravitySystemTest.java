package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.GravityComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class GravitySystemTest {

    @Test
    void update_updatesEntitiesWithGravity(DefaultEntityEngine entityEngine, GameLoop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(new PhysicsBodyComponent());
        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 10)));

        entityEngine.add(body, gravity);
        entityEngine.add(new GravitySystem());

        entityEngine.update();

        Vector momentum = body.get(PhysicsBodyComponent.class).momentum;
        assertThat(momentum).isEqualTo(Vector.of(0, 5));
    }

}
