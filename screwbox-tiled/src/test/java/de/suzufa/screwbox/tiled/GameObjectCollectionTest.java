package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameObjectCollectionTest {

    private GameObjectCollection gameObjectCollection;

    @BeforeEach
    void beforeEach() {
        Map map = Map.fromJson("underworld_map.json");
        gameObjectCollection = map.objects();
    }

    @Test
    void allObjects_returnsAllObjects() {
        assertThat(gameObjectCollection.all())
                .hasSize(7);
    }

    @Test
    void findByName_nonFound_returnsEmptyOptional() {
        assertThat(gameObjectCollection.findByName("unknown")).isEmpty();
    }

    @Test
    void findByName_oneFound_returnsObject() {
        assertThat(gameObjectCollection.findByName("testsquare")).isPresent();
    }

    @Test
    void findAllWithName_multipleFound_returnsAll() {
        assertThat(gameObjectCollection.findAllWithName("dummy"))
                .hasSize(2);
    }

    @Test
    void findAllWithName_noneFound_returnsEmptyList() {
        assertThat(gameObjectCollection.findAllWithName("unknown")).isEmpty();
    }

    @Test
    void findById_nonFound_returnsEmptyOptional() {
        assertThat(gameObjectCollection.findById(99)).isEmpty();
    }

    @Test
    void findById_found_returnsObject() {
        assertThat(gameObjectCollection.findById(7)).isPresent();
    }
}
