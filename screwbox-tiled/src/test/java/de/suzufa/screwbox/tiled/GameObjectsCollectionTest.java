package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameObjectsCollectionTest {

    private GameObjectsCollection gameObjectsCollection;

    @BeforeEach
    void beforeEach() {
        Map map = Map.fromJson("underworld_map.json");
        gameObjectsCollection = map.objects();
    }

    @Test
    void allObjects_returnsAllObjects() {
        assertThat(gameObjectsCollection.all())
                .hasSize(7);
    }

    @Test
    void findByName_nonFound_returnsEmptyOptional() {
        assertThat(gameObjectsCollection.findByName("unknown")).isEmpty();
    }

    @Test
    void findByName_oneFound_returnsObject() {
        assertThat(gameObjectsCollection.findByName("testsquare")).isPresent();
    }

    @Test
    void findAllWithName_multipleFound_returnsAll() {
        assertThat(gameObjectsCollection.findAllWithName("dummy"))
                .hasSize(2);
    }

    @Test
    void findAllWithName_noneFound_returnsEmptyList() {
        assertThat(gameObjectsCollection.findAllWithName("unknown")).isEmpty();
    }

    @Test
    void findById_nonFound_returnsEmptyOptional() {
        assertThat(gameObjectsCollection.findById(99)).isEmpty();
    }

    @Test
    void findById_found_returnsObject() {
        assertThat(gameObjectsCollection.findById(7)).isPresent();
    }
}
