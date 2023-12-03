package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RaycastBuilderTest {

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        var raycastBuilder = new RaycastBuilder(null, Vector.zero());
        Archetype archetype = Archetype.of(PhysicsBodyComponent.class);

        assertThatThrownBy(() -> raycastBuilder.checkingFor(archetype))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot raycast for Archetypes without TransformComponent");
    }
}
