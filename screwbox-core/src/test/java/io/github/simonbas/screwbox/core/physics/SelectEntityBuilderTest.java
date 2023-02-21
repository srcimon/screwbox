package io.github.simonbas.screwbox.core.physics;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.github.simonbas.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectEntityBuilderTest {

    @Mock
    Entities entities;

    SelectEntityBuilder selectEntityBuilder;

    @BeforeEach
    void beforeEach() {
        selectEntityBuilder = new SelectEntityBuilder(entities, Vector.$(40, 60));
    }

    @Test
    void entityBuilderSelectingInBounds_findsOnlyEntitiesInBounds() {
        Entity first = boxAt(0, 10);
        Entity second = boxAt(55, 45);
        Entity notInBounds = boxAt(155, 45);
        when(entities.fetchAll(Archetype.of(TransformComponent.class, ColliderComponent.class)))
                .thenReturn(List.of(first, second, notInBounds));

        SelectEntityBuilder boundsEntityBuilder = new SelectEntityBuilder(entities, $$(0, 0, 100, 100));

        Assertions.assertThat(boundsEntityBuilder.selectAll()).contains(first, second).doesNotContain(notInBounds);

    }

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        Archetype noTransform = Archetype.of(PhysicsBodyComponent.class);

        assertThatThrownBy(() -> selectEntityBuilder.checkingFor(noTransform))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot select entities by position for Archetypes without TransformComponent");
    }

    @Test
    void selectAny_noEntityAtPosition_returnsEmpty() {
        when(entities.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(
                        boxAt(55, 45),
                        boxAt(25, 45).add(new RenderComponent(0)),
                        boxAt(25, 15)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAny();

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void selectAny_entityAtPosition_returnsEntity() {
        when(entities.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(boxAt(39, 59)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAny();

        Assertions.assertThat(result).isPresent();
    }
    
    @Test
    void selectAll_entitiesAtPosition_returnsAllEntitiesAtPosition() {
        Entity first = boxAt(39, 59);
        Entity second = boxAt(38, 58);
        Entity notAtPosition = boxAt(0, 0);
        when(entities.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(first, second, notAtPosition));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAll();

        Assertions.assertThat(result).contains(first, second).doesNotContain(notAtPosition);

    }

    private Entity boxAt(double x, double y) {
        Bounds bounds = Bounds.atPosition(x, y, 20, 20);
        return new Entity()
                .add(new ColliderComponent())
                .add(new TransformComponent(bounds));
    }

}
