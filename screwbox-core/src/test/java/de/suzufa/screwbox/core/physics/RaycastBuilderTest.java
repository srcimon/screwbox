package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;

class RaycastBuilderTest {

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        var raycastBuilder = new RaycastBuilder(null, Vector.zero());

        assertThatThrownBy(() -> raycastBuilder
                .checkingFor(Archetype.of(PhysicsBodyComponent.class)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("cannot raycast for Archetypes without TransformComponent");
    }
}
