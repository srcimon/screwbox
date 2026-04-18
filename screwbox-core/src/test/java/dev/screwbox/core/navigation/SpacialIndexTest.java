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

    private static List<Entity> createEntities(int size) {
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            entities.add(new Entity().bounds($$(i * 10, 0, 1,1)));
        }
        return entities;
    }
    @Test
    void registry_searchWithMoreThanFiftyEntities_hasCellSizeMatchingSearchRadius() {
        spacialIndex.refresh(createEntities(51));
        spacialIndex.findEntities($(20, 10), 246);
        assertThat(spacialIndex.registry()).isPresent();
        //TODO implement
    }
}
