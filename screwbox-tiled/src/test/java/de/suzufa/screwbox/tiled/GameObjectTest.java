package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

class GameObjectTest {

    private GameObject image;
    private GameObject pointer;
    private GameObject square;

    @BeforeEach
    void beforeEach() {
        var map = Map.fromJson("underworld_map.json");
        image = map.objectWithName("testimage").orElseThrow();
        pointer = map.objectWithName("testpoint").orElseThrow();
        square = map.objectWithName("testsquare").orElseThrow();
    }

    @Test
    void postion_allObjectTypes_returnsPosition() {
        assertThat(image.position()).isEqualTo(Vector.of(8, 8));
        assertThat(pointer.position()).isEqualTo(Vector.of(32, 16));
        assertThat(square.position()).isEqualTo(Vector.of(24, 48));
    }

    @Test
    void bounds_allObjectTypes_returnsBounds() {
        assertThat(image.bounds()).isEqualTo(Bounds.atPosition(8, 8, 16, 16));
        assertThat(pointer.bounds()).isEqualTo(Bounds.atPosition(32, 16, 0, 0));
        assertThat(square.bounds()).isEqualTo(Bounds.atPosition(24, 48, 16, 32));
    }

    @Test
    void id_returnsObjectId() {
        assertThat(image.id()).isEqualTo(3);
    }

    @Test
    void name_returnsName() {
        assertThat(pointer.name()).isEqualTo("testpoint");
    }

    @Test
    void properties_returnsCorrectProperties() {
        Properties properties = pointer.properties();

        assertThat(properties.force("material")).isEqualTo("ice");
        assertThat(properties.forceDouble("length")).isEqualTo(66.6);
        assertThat(properties.forceInt("numbervalue")).isEqualTo(2);
    }

    @Test
    void layer_returnsObjectLayer() {
        assertThat(image.layer().order()).isEqualTo(2);
    }

    @Test
    void toString_containsName() {
        assertThat(image).hasToString("GameObject [name=testimage]");
    }
}