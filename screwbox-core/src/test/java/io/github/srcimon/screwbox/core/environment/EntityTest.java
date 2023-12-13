package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.RigidBodyComponent;
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
        entity.add(new RigidBodyComponent())
                .add(new ColliderComponent());

        assertThat(entity.getAll()).hasSize(2);
    }

    @Test
    void add_componentClassNotPresent_notifiesListeners() {
        var listener = Mockito.mock(EntityListener.class);
        entity.registerListener(listener);

        entity.add(new RigidBodyComponent());

        verify(listener).componentAdded(argThat(event -> event.entity().equals(entity)));
    }

    @Test
    void add_componentClassAlreadyPresent_throwsException() {
        Component component = new RigidBodyComponent();
        entity.add(component);

        assertThatThrownBy(() -> entity.add(component))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("component already present: PhysicsBodyComponent");
    }

    @Test
    void get_componentPresent_returnsComponent() {
        Component component = new RigidBodyComponent();
        entity.add(component);

        RigidBodyComponent result = entity.get(RigidBodyComponent.class);

        assertThat(result).isEqualTo(component);
    }

    @Test
    void get_componentNotPresent_returnsNull() {
        Assertions.assertThat(entity.get(RigidBodyComponent.class)).isNull();
    }

    @Test
    void add_addsComponentsToExistingEntity() {
        var physicsBodyComponent = new RigidBodyComponent();
        var colliderComponent = new ColliderComponent();
        Entity entity = new Entity().add(physicsBodyComponent, colliderComponent);

        assertThat(entity.getAll()).contains(physicsBodyComponent, colliderComponent);
    }

    @Test
    void hasComponent_componentNotPresent_returnsFalse() {
        assertThat(entity.hasComponent(RigidBodyComponent.class)).isFalse();
    }

    @Test
    void hasComponent_componentPresent_returnsTrue() {
        entity.add(new RigidBodyComponent());

        assertThat(entity.hasComponent(RigidBodyComponent.class)).isTrue();
    }

    @Test
    void remove_componentPresent_removesComponent() {
        entity.add(new RigidBodyComponent());

        entity.remove(RigidBodyComponent.class);

        assertThat(entity.hasComponent(RigidBodyComponent.class)).isFalse();
    }

    @Test
    void remove_componentPresent_notifiesListeners() {
        var listener = Mockito.mock(EntityListener.class);
        entity.registerListener(listener);
        entity.add(new RigidBodyComponent());

        entity.remove(RigidBodyComponent.class);

        verify(listener).componentRemoved(argThat(event -> event.entity().equals(entity)));
    }

    @Test
    void componentCount_returnsCountOfComponents() {
        entity.add(new RigidBodyComponent());
        entity.add(new ColliderComponent());

        assertThat(entity.componentCount()).isEqualTo(2);
    }

    @Test
    void getComponentClasses_returnsClassesOfComponents() {
        entity.add(new RigidBodyComponent());
        entity.add(new ColliderComponent());

        assertThat(entity.getComponentClasses())
                .contains(RigidBodyComponent.class, ColliderComponent.class)
                .hasSize(2);
    }

    @Test
    void isEmpty_noComponents_true() {
        assertThat(entity.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_hasComponents_false() {
        entity.add(new RigidBodyComponent());

        assertThat(entity.isEmpty()).isFalse();
    }
}
