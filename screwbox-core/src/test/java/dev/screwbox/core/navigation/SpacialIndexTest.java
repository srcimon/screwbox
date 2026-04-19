package dev.screwbox.core.navigation;

import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;

class SpacialIndexTest {

    SpacialIndex spacialIndex;

    @BeforeEach
    void setUp() {
        spacialIndex = new SpacialIndex();
    }

    @Test
    void findEntities_noFilterAndNoEntities_isEmpty() {
        assertThat(spacialIndex.findEntities($(20, 10), 1024)).isEmpty();

    }

    @Test
    void registry_noSearchWithMoreThanFiftyEntities_isEmpty() {
        spacialIndex.refresh(createEntities(49));
        spacialIndex.findEntities($(20, 10), 1024);
        assertThat(spacialIndex.registry()).isEmpty();
    }

    @Test
    void findEntities_largerRadiusThanBefore_increasesRegistryCellSize() {
        spacialIndex.refresh(createEntities(51));
        spacialIndex.findEntities($(20, 10), 246);

        assertThat(spacialIndex.registry()).hasValueSatisfying(registry ->
            assertThat(registry.cellSize()).isEqualTo(256));

        spacialIndex.findEntities($(20, 10), 300);

        assertThat(spacialIndex.registry()).hasValueSatisfying(registry ->
            assertThat(registry.cellSize()).isEqualTo(512));
    }

    @Test
    void refresh_setsNewEntitiesToSearchAndClearsRegistry() {
        spacialIndex.refresh(createEntities(51));
        spacialIndex.findEntities($(20, 10), 246);

        spacialIndex.refresh(createEntities(4));

        assertThat(spacialIndex.registry()).isEmpty();
        assertThat(spacialIndex.findEntities($(20, 10), 999999)).hasSize(4);
    }

    @Test
    void findEntities_someEntitiesInRange_returnsOnlyInRangeEntities() {
        spacialIndex.refresh(createEntities(51));

        assertThat(spacialIndex.findEntities($(20, 10), 20)).hasSize(3)
            .allMatch(entity -> entity.position().distanceTo($(20, 10)) <= 20);
    }

    @Test
    void findEntities_usingFilter_returnsOnlyEntitiesMatchingFilter() {
        spacialIndex.refresh(createEntities(51));

        assertThat(spacialIndex.findEntities($(20, 10), 100, e -> e.forceId() < 3)).hasSize(3);
    }

    @Test
    void findEntities_smallSearchRadiusAfterReset_searchesWithLastUseCellSize() {
        spacialIndex.refresh(createEntities(60));
        spacialIndex.findEntities($(20, 10), 1000);
        spacialIndex.refresh(createEntities(60));
        assertThat(spacialIndex.registry()).isEmpty();

        spacialIndex.findEntities($(20, 10), 20);
        assertThat(spacialIndex.registry()).hasValueSatisfying(registry ->
            assertThat(registry.cellSize()).isEqualTo(1024));
    }

    private static List<Entity> createEntities(int size) {
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            entities.add(new Entity(i).bounds($$(i * 10, 0, 1, 1)));
        }
        return entities;
    }
}
