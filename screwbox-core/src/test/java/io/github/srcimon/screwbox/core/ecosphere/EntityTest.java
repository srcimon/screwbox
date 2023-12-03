package io.github.srcimon.screwbox.core.ecosphere;

import io.github.srcimon.screwbox.core.ecosphere.components.ColliderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

class EntityTest {

    private Entity entity;

    @BeforeEach
    void beforeEach() {
        this.entity = new Entity();
    }

    @Test
    void newEntity_withoutId_hasNoId() {
        assertThat(entity.id()).isEmpty();
    }

    @Test
    void newEntiy_withId_hasId() {
        assertThat(new Entity(123).id()).isEqualTo(Optional.of(123));
    }

    @Test
    void add_componentClassNotPresent_addsComponent() {
        entity.add(new PhysicsBodyComponent())
                .add(new ColliderComponent());

        assertThat(entity.getAll()).hasSize(2);
    }

    @Test
    void add_componentClassNotPresent_notifiesListeners() {
        var listener = Mockito.mock(EntityListener.class);
        entity.registerListener(listener);

        entity.add(new PhysicsBodyComponent());

        verify(listener).componentAdded(argThat(event -> event.entity().equals(entity)));
    }

    @Test
    void add_componentClassAlreadyPresent_throwsException() {
        Component component = new PhysicsBodyComponent();
        entity.add(component);

        assertThatThrownBy(() -> entity.add(component))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("component already present: PhysicsBodyComponent");
    }

    @Test
    void get_componentPresent_returnsComponent() {
        Component component = new PhysicsBodyComponent();
        entity.add(component);

        PhysicsBodyComponent result = entity.get(PhysicsBodyComponent.class);

        assertThat(result).isEqualTo(component);
    }

    @Test
    void get_componentNotPresent_returnsNull() {
        Assertions.assertThat(entity.get(PhysicsBodyComponent.class)).isNull();
    }

    @Test
    void add_addsComponentsToExistingEntity() {
        var physicsBodyComponent = new PhysicsBodyComponent();
        var colliderComponent = new ColliderComponent();
        Entity entity = new Entity().add(physicsBodyComponent, colliderComponent);

        assertThat(entity.getAll()).contains(physicsBodyComponent, colliderComponent);
    }

    @Test
    void hasComponent_componentNotPresent_returnsFalse() {
        assertThat(entity.hasComponent(PhysicsBodyComponent.class)).isFalse();
    }

    @Test
    void hasComponent_componentPresent_returnsTrue() {
        entity.add(new PhysicsBodyComponent());

        assertThat(entity.hasComponent(PhysicsBodyComponent.class)).isTrue();
    }

    @Test
    void remove_componentPresent_removesComponent() {
        entity.add(new PhysicsBodyComponent());

        entity.remove(PhysicsBodyComponent.class);

        assertThat(entity.hasComponent(PhysicsBodyComponent.class)).isFalse();
    }

    @Test
    void remove_componentPresent_notifiesListeners() {
        var listener = Mockito.mock(EntityListener.class);
        entity.registerListener(listener);
        entity.add(new PhysicsBodyComponent());

        entity.remove(PhysicsBodyComponent.class);

        verify(listener).componentRemoved(argThat(event -> event.entity().equals(entity)));
    }

    @Test
    void componentCount_returnsCountOfComponents() {
        entity.add(new PhysicsBodyComponent());
        entity.add(new ColliderComponent());

        assertThat(entity.componentCount()).isEqualTo(2);
    }

    @Test
    void getComponentClasses_returnsClassesOfComponents() {
        entity.add(new PhysicsBodyComponent());
        entity.add(new ColliderComponent());

        assertThat(entity.getComponentClasses())
                .contains(PhysicsBodyComponent.class, ColliderComponent.class)
                .hasSize(2);
    }

    @Test
    void isEmpty_noComponents_true() {
        assertThat(entity.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_hasComponents_false() {
        entity.add(new PhysicsBodyComponent());

        assertThat(entity.isEmpty()).isFalse();
    }
}
