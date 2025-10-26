package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@MockitoSettings
class SelectEntityBuilderTest {

    @Mock
    Environment environment;

    SelectEntityBuilder selectEntityBuilder;

    @BeforeEach
    void beforeEach() {
        selectEntityBuilder = new SelectEntityBuilder(environment, Vector.$(40, 60));
    }

    @Test
    void entityBuilderSelectingInBounds_findsOnlyEntitiesInBounds() {
        Entity first = boxAt(0, 10);
        Entity second = boxAt(55, 45);
        Entity notInBounds = boxAt(155, 45);
        when(environment.fetchAll(Archetype.of(TransformComponent.class)))
                .thenReturn(List.of(first, second, notInBounds));

        SelectEntityBuilder boundsEntityBuilder = new SelectEntityBuilder(environment, $$(0, 0, 100, 100));

        assertThat(boundsEntityBuilder.selectAll()).contains(first, second).doesNotContain(notInBounds);

    }

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        Archetype noTransform = Archetype.of(PhysicsComponent.class);

        assertThatThrownBy(() -> selectEntityBuilder.checkingFor(noTransform))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot select entities by position for Archetypes without TransformComponent");
    }

    @Test
    void selectAny_noEntityAtPosition_returnsEmpty() {
        when(environment.fetchAll(Archetype.of(TransformComponent.class, PhysicsComponent.class)))
                .thenReturn(List.of(
                        boxAt(55, 45),
                        boxAt(25, 45).add(new RenderComponent(0)),
                        boxAt(25, 15)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAny();

        assertThat(result).isEmpty();
    }

    @Test
    void selectAny_entityAtPosition_returnsEntity() {
        when(environment.fetchAll(Archetype.of(TransformComponent.class, PhysicsComponent.class)))
                .thenReturn(List.of(boxAt(39, 59)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsComponent.class))
                .ignoringEntitiesHaving(RenderComponent.class)
                .selectAny();

        assertThat(result).isPresent();
    }

    @Test
    void selectAll_entitiesAtPosition_returnsAllEntitiesAtPosition() {
        Entity first = boxAt(39, 59);
        Entity second = boxAt(38, 58);
        Entity notAtPosition = boxAt(0, 0);
        when(environment.fetchAll(Archetype.of(TransformComponent.class, PhysicsComponent.class)))
                .thenReturn(List.of(first, second, notAtPosition));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsComponent.class))
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
