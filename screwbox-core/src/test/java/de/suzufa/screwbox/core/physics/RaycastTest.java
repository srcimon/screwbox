package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;
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
class RaycastTest {

    @Mock
    private EntityEngine entityEngine;

    private RaycastBuilder raycastBuilder;

    @BeforeEach
    void beforeEach() {
        raycastBuilder = new RaycastBuilder(entityEngine, Vector.zero());
    }

    @Test
    void hasHit_rayIntersectsBox_isTrue() {
        when(entityEngine.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(50, 0)));

        var result = raycastBuilder
                .castingHorizontal(200)
                .hasHit();

        assertThat(result).isTrue();
    }

    @Test
    void hasHit_checkingForNotCrossedBorders_isFalse() {
        when(entityEngine.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(50, 0)));

        var result = raycastBuilder
                .checkingBorders(Borders.TOP_ONLY)
                .castingHorizontal(200)
                .hasHit();

        assertThat(result).isFalse();
    }

    @Test
    void selectAllEnties_rayHitsMultipeEntitiesButSomeAreIgnored_returnsHits() {
        var physicsEntites = Archetype.of(TransformComponent.class, PhysicsBodyComponent.class);

        Entity foundA = boxAt(0, 100);
        Entity foundB = boxAt(0, 300);
        Entity ignoredEntity = boxAt(0, 100);
        Entity ignoredBecauseOfComponent = boxAt(0, 200).add(new SpriteComponent(0));
        Entity ignoredBecauseNotInBounds = boxAt(0, 600);

        when(entityEngine.fetchAll(physicsEntites)).thenReturn(List.of(
                ignoredBecauseOfComponent,
                foundA,
                ignoredEntity,
                foundB,
                ignoredBecauseNotInBounds));

        var result = raycastBuilder
                .checkingFor(physicsEntites)
                .ignoringEntitiesHaving(SpriteComponent.class)
                .ignoringEntitesNotIn(Bounds.atOrigin(0, 0, 200, 500))
                .ignoringEntities(ignoredEntity)
                .castingVertical(1000)
                .selectAllEntities();

        assertThat(result).hasSize(2).contains(foundA, foundB);
    }

    @Test
    void nearestHit_multipleHits_returnsNearestHitPosition() {
        when(entityEngine.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(0, 50),
                boxAt(0, 150),
                boxAt(0, 250)));

        var result = raycastBuilder
                .checkingBorders(Borders.TOP_ONLY)
                .castingTo(0, 400)
                .nearestHit();

        assertThat(result).contains(Vector.of(0, 40));
    }

    @Test
    void selectAnyEntity_noHit_isEmpty() {
        when(entityEngine.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(0, 50),
                boxAt(0, 150),
                boxAt(0, 250)));

        var result = raycastBuilder
                .checkingBorders(Borders.TOP_ONLY)
                .castingTo(Vector.of(-200, -400))
                .selectAnyEntity();

        assertThat(result).isEmpty();
    }

    @Test
    void selectAnyEntity_multiplePossibleHits_returnsAnyOfHits() {
        when(entityEngine.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(0, 30),
                boxAt(0, 50),
                boxAt(0, 40)));

        var result = raycastBuilder
                .castingVertical(200)
                .selectAnyEntity();

        assertThat(result).isPresent();
    }

    private Archetype defaultArchetype() {
        return Archetype.of(TransformComponent.class, ColliderComponent.class);
    }

    private Entity boxAt(double x, double y) {
        Bounds bounds = Bounds.atPosition(x, y, 20, 20);
        return new Entity()
                .add(new ColliderComponent())
                .add(new TransformComponent(bounds));
    }
}
