package de.suzufa.screwbox.core.physics;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

@ExtendWith(MockitoExtension.class)
class SelectEntityBuilderTest {

    @Mock
    Entities entities;

    SelectEntityBuilder selectEntityBuilder;

    @BeforeEach
    void beforeEach() {
        selectEntityBuilder = new SelectEntityBuilder(entities, $(40, 60));
    }

    @Test
    void entityBuilderSelectingInBounds_findsOnlyEntitiesInBounds() {
        Entity first = boxAt(0, 10);
        Entity second = boxAt(55, 45);
        Entity notInBounds = boxAt(155, 45);
        when(entities.fetchAll(Archetype.of(TransformComponent.class, ColliderComponent.class)))
                .thenReturn(List.of(first, second, notInBounds));

        SelectEntityBuilder boundsEntityBuilder = new SelectEntityBuilder(entities, $$(0, 0, 100, 100));

        assertThat(boundsEntityBuilder.selectAll()).contains(first, second).doesNotContain(notInBounds);

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

        assertThat(result).isEmpty();
    }

    @Test
    void selectAny_entityAtPosition_returnsEntity() {
        when(entities.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(boxAt(39, 59)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAny();

        assertThat(result).isPresent();
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

        assertThat(result).contains(first, second).doesNotContain(notAtPosition);

    }

    private Entity boxAt(double x, double y) {
        Bounds bounds = Bounds.atPosition(x, y, 20, 20);
        return new Entity()
                .add(new ColliderComponent())
                .add(new TransformComponent(bounds));
    }

}
