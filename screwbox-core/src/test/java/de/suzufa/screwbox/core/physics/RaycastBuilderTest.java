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
        Archetype archetype = Archetype.of(PhysicsBodyComponent.class);

        assertThatThrownBy(() -> raycastBuilder.checkingFor(archetype))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot raycast for Archetypes without TransformComponent");
    }
}
