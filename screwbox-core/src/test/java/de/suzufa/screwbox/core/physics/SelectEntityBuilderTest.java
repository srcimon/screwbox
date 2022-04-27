package de.suzufa.screwbox.core.physics;

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
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

@ExtendWith(MockitoExtension.class)
class SelectEntityBuilderTest {

    @Mock
    EntityEngine entityEngine;

    SelectEntityBuilder selectEntityBuilder;

    @BeforeEach
    void beforeEach() {
        selectEntityBuilder = new SelectEntityBuilder(entityEngine, Vector.of(40, 60));
    }

    @Test
    void checkingFor_noTransformComponent_throwsException() {
        Archetype noTransform = Archetype.of(PhysicsBodyComponent.class);

        assertThatThrownBy(() -> selectEntityBuilder.checkingFor(noTransform))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot select entities by position for Archetypes without TransformComponent");
    }

    @Test
    void selectAnyEntity_noEntityAtPosition_returnsEmpty() {
        when(entityEngine.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(
                        boxAt(55, 45),
                        boxAt(25, 45).add(new SpriteComponent(0)),
                        boxAt(25, 15)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(SpriteComponent.class)
                .selectAnyEntity();

        assertThat(result).isEmpty();
    }

    @Test
    void selectAnyEntity_entityAtPosition_returnsEntity() {
        when(entityEngine.fetchAll(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class)))
                .thenReturn(List.of(boxAt(39, 59)));

        var result = selectEntityBuilder
                .checkingFor(Archetype.of(TransformComponent.class, PhysicsBodyComponent.class))
                .ignoringEntitiesHaving(SpriteComponent.class)
                .selectAnyEntity();

        assertThat(result).isPresent();
    }

    private Entity boxAt(double x, double y) {
        Bounds bounds = Bounds.atPosition(x, y, 20, 20);
        return new Entity()
                .add(new ColliderComponent())
                .add(new TransformComponent(bounds));
    }

}
