package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
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
