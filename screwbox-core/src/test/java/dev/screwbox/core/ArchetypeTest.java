package dev.screwbox.core;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchetypeTest {

    @Test
    void equals_identicalClasses_returnsTrue() {
        var typeA = Archetype.ofSpacial(PhysicsComponent.class);
        var typeB = Archetype.of(TransformComponent.class, PhysicsComponent.class);

        assertThat(typeA).isEqualTo(typeB);
    }

    @Test
    void equals_oneArchetypeIsMissingClass_returnsFalse() {
        var typeA = Archetype.of(PhysicsComponent.class, TransformComponent.class);
        var typeB = Archetype.of(PhysicsComponent.class);

        assertThat(typeA).isNotEqualTo(typeB);
    }

    @Test
    void matches_entityIsMissingComponent_returnsFalse() {
        var archetype = Archetype.of(TransformComponent.class);

        Entity entity = new Entity().add(new PhysicsComponent());

        assertThat(archetype.matches(entity)).isFalse();
    }

    @Test
    void matches_entityHasArchetypeComponents_returnsTrue() {
        var archetype = Archetype.of(PhysicsComponent.class, TransformComponent.class);

        Entity entity = new Entity()
                .add(new StaticColliderComponent())
                .add(new PhysicsComponent())
                .add(new TransformComponent(Bounds.atPosition(0, 0, 0, 0)));

        assertThat(archetype.matches(entity)).isTrue();
    }

    @Test
    void contains_doesntContainClass_isFalse() {
        var archetype = Archetype.of(PhysicsComponent.class, TransformComponent.class);

        assertThat(archetype.contains(RenderComponent.class)).isFalse();
    }

    @Test
    void contains_containClass_isTrue() {
        var archetype = Archetype.of(PhysicsComponent.class, TransformComponent.class);

        assertThat(archetype.contains(TransformComponent.class)).isTrue();
    }

    @Test
    void ofSpacial_withOtherComponent_containsTransformComponent() {
        var archetype = Archetype.ofSpacial(PhysicsComponent.class);

        assertThat(archetype.contains(PhysicsComponent.class)).isTrue();
        assertThat(archetype.contains(TransformComponent.class)).isTrue();
    }
}
