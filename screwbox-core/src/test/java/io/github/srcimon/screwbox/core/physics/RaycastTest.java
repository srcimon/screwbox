package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaycastTest {

    @Mock
    private Entities entities;

    private RaycastBuilder raycastBuilder;

    @BeforeEach
    void beforeEach() {
        raycastBuilder = new RaycastBuilder(entities, Vector.zero());
    }

    @Test
    void hasHit_rayIntersectsBox_isTrue() {
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(50, 0)));

        var result = raycastBuilder
                .castingHorizontal(200)
                .hasHit();

        assertThat(result).isTrue();
    }

    @Test
    void hasHit_checkingForNotCrossedBorders_isFalse() {
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        Entity ignoredBecauseOfPosition = boxAt(12, 200);
        Entity ignoredBecauseOfComponent = boxAt(0, 200).add(new RenderComponent(0));
        Entity ignoredBecauseNotInBounds = boxAt(0, 600);

        when(entities.fetchAll(physicsEntites)).thenReturn(List.of(
                ignoredBecauseOfComponent,
                foundA,
                ignoredEntity,
                foundB,
                ignoredBecauseNotInBounds,
                ignoredBecauseOfPosition));

        var result = raycastBuilder
                .checkingFor(physicsEntites)
                .ignoringEntitiesMatching(e -> e.get(TransformComponent.class).bounds.position().x() == 12)
                .ignoringEntitiesHaving(RenderComponent.class)
                .ignoringEntitesNotIn(Bounds.atOrigin(0, 0, 200, 500))
                .ignoringEntities(ignoredEntity)
                .castingVertical(1000)
                .selectAllEntities();

        assertThat(result).hasSize(2).contains(foundA, foundB);
    }

    @Test
    void findHits_multipleHits_returnsAllHits() {
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(0, 50),
                boxAt(0, 150),
                boxAt(0, 250)));

        var result = raycastBuilder
                .checkingBorders(Borders.ALL)
                .castingTo(0, 400)
                .findHits();

        assertThat(result).hasSize(6).contains(Vector.of(0, 40));
    }
    @Test
    void nearestHit_multipleHits_returnsNearestHitPosition() {
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        when(entities.fetchAll(defaultArchetype())).thenReturn(List.of(
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
