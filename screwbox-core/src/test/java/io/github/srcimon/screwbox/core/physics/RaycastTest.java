package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
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
    private Environment environment;

    private RaycastBuilder raycastBuilder;

    @BeforeEach
    void beforeEach() {
        raycastBuilder = new RaycastBuilder(environment, Vector.zero());
    }

    @Test
    void ray_returnsRay() {
        var raycast = raycastBuilder.castingTo(Vector.x(20));

        assertThat(raycast.ray()).isEqualTo(Line.between(Vector.zero(), Vector.x(20)));
    }

    @Test
    void hasHit_rayIntersectsBox_isTrue() {
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(50, 0)));

        var result = raycastBuilder
                .castingHorizontal(200)
                .hasHit();

        assertThat(result).isTrue();
    }

    @Test
    void hasHit_checkingForNotCrossedBorders_isFalse() {
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
                boxAt(50, 0)));

        var result = raycastBuilder
                .checkingBorders(Borders.TOP_ONLY)
                .castingHorizontal(200)
                .hasHit();

        assertThat(result).isFalse();
    }

    @Test
    void selectAllEnties_rayHitsMultipeEntitiesButSomeAreIgnored_returnsHits() {
        var physicsEntites = Archetype.of(TransformComponent.class, PhysicsComponent.class);

        Entity foundA = boxAt(0, 100);
        Entity foundB = boxAt(0, 300);
        Entity ignoredEntity = boxAt(0, 100);
        Entity ignoredBecauseOfPosition = boxAt(12, 200);
        Entity ignoredBecauseOfComponent = boxAt(0, 200).add(new RenderComponent(0));
        Entity ignoredBecauseNotInBounds = boxAt(0, 600);

        when(environment.fetchAll(physicsEntites)).thenReturn(List.of(
                ignoredBecauseOfComponent,
                foundA,
                ignoredEntity,
                foundB,
                ignoredBecauseNotInBounds,
                ignoredBecauseOfPosition));

        var result = raycastBuilder
                .checkingFor(physicsEntites)
                .ignoringEntitiesMatching(e -> e.position().x() == 12)
                .ignoringEntitiesHaving(RenderComponent.class)
                .ignoringEntitesNotIn(Bounds.atOrigin(0, 0, 200, 500))
                .ignoringEntities(ignoredEntity)
                .castingVertical(1000)
                .selectAllEntities();

        assertThat(result).hasSize(2).contains(foundA, foundB);
    }

    @Test
    void findHits_multipleHits_returnsAllHits() {
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
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
        when(environment.fetchAll(defaultArchetype())).thenReturn(List.of(
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
