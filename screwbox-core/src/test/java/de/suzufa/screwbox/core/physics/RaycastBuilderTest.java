package de.suzufa.screwbox.core.physics;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
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
