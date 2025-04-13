package dev.screwbox.core.physics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RaycastBuilderTest {

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        var raycastBuilder = new RaycastBuilder(null, Vector.zero());
        Archetype archetype = Archetype.of(PhysicsComponent.class);

        assertThatThrownBy(() -> raycastBuilder.checkingFor(archetype))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot raycast for Archetypes without TransformComponent");
    }
}
