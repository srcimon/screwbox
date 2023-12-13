package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.ForwardSignalComponent;
import io.github.srcimon.screwbox.core.environment.physics.RigidBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchetypeTest {

    @Test
    void equals_identicalClasses_returnsTrue() {
        var typeA = Archetype.of(RigidBodyComponent.class, TransformComponent.class);
        var typeB = Archetype.of(TransformComponent.class, RigidBodyComponent.class);

        assertThat(typeA).isEqualTo(typeB);
    }

    @Test
    void equals_oneArchetypeIsMissingClass_returnsFalse() {
        var typeA = Archetype.of(RigidBodyComponent.class, TransformComponent.class);
        var typeB = Archetype.of(RigidBodyComponent.class);

        assertThat(typeA).isNotEqualTo(typeB);
    }

    @Test
    void matches_entityIsMissingComponent_returnsFalse() {
        var archetype = Archetype.of(TransformComponent.class);

        Entity entity = new Entity().add(new RigidBodyComponent());

        assertThat(archetype.matches(entity)).isFalse();
    }

    @Test
    void matches_entityHasArchetypeComponents_returnsTrue() {
        var archetype = Archetype.of(RigidBodyComponent.class, TransformComponent.class);

        Entity entity = new Entity()
                .add(new ForwardSignalComponent())
                .add(new RigidBodyComponent())
                .add(new TransformComponent(Bounds.atPosition(0, 0, 0, 0)));

        assertThat(archetype.matches(entity)).isTrue();
    }

    @Test
    void contains_doesntContainClass_isFalse() {
        var archetype = Archetype.of(RigidBodyComponent.class, TransformComponent.class);

        assertThat(archetype.contains(RenderComponent.class)).isFalse();
    }

    @Test
    void contains_containClass_isTrue() {
        var archetype = Archetype.of(RigidBodyComponent.class, TransformComponent.class);

        assertThat(archetype.contains(TransformComponent.class)).isTrue();
    }
}
