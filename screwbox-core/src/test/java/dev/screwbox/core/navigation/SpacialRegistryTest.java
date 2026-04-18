package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpacialRegistryTest {

    private static final List<Entity> ENTITIES = List.of(
        new Entity(1).bounds(Bounds.atPosition(40, 20, 1, 1)),
        new Entity(2).bounds(Bounds.atPosition(-50, 10, 1, 1)),
        new Entity(3).bounds(Bounds.atPosition(0, 1.2, 1, 1)),
        new Entity(4).bounds(Bounds.atPosition(2.4, 1.2, 1, 1)),
        new Entity(5).bounds(Bounds.atPosition(-140, 29, 1, 1))
    );

    @Test
    void newInstance_negativeCellSize_throwsException() {
        List<Entity> entities = Collections.emptyList();

        assertThatThrownBy(() -> new SpacialRegistry(-4, entities))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be positive (actual value: -4.0)");
    }

    @Test
    void newInstance_entityWithoutTransformComponent_throwsException() {
        List<Entity> entities = List.of(new Entity());

        assertThatThrownBy(() -> new SpacialRegistry(16, entities))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("entity has no TransformComponent");
    }

    @Test
    void queryBucket_bucketNotEmpty_containsEntities() {
        var registry = new SpacialRegistry(16, ENTITIES);

        var entities = registry.queryBucket($(0,0));

        assertThat(entities).hasSize(2)
            .anyMatch(entity -> entity.forceId() == 3)
            .anyMatch(entity -> entity.forceId() == 4);
    }

    @Test
    void queryBucket_noEntitiesNearby_canBeEmptyButMustNotBecauseIndexCanContainDuplicates() {
        var registry = new SpacialRegistry(16, ENTITIES);

        var entities = registry.queryBucket($(-410,0));

        assertThat(entities).isEmpty();
    }

    @Test
    void queryLocalBuckets_entityWithinLocalBucket_containsEntitiesFromAllBuckets() {
        var registry = new SpacialRegistry(16, ENTITIES);

        var entities = registry.queryLocalBuckets($(0,0));

        assertThat(entities).hasSize(3)
            .anyMatch(entity -> entity.forceId() == 3)
            .anyMatch(entity -> entity.forceId() == 4)
            .anyMatch(entity -> entity.forceId() == 2);
    }

    @Test
    void queryLocalBuckets_allEntitiesWithinRange_doesNotContainDuplicates() {
        var registry = new SpacialRegistry(150, ENTITIES);

        var entities = registry.queryLocalBuckets($(0,0));

        assertThat(entities).containsExactlyInAnyOrderElementsOf(ENTITIES);
    }

    @Test
    void cellSize_cellSize16_is16() {
        var registry = new SpacialRegistry(16, ENTITIES);

        assertThat(registry.cellSize()).isEqualTo(16);
    }
}
