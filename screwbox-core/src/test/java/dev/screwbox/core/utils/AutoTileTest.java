package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AutoTileTest {

    //TODO ramp up tests and rename all
    @Test
    void createIndex_XXX() {
        var index = AutoTile.createIndex(Offset.at(10, 10), offset -> offset.equals(Offset.at(11, 10)));
        assertThat(index.index3x3Minimal()).isEqualTo(4);
    }

    @Test
    void createIndex_XXX2() {
        var index = AutoTile.createIndex(Offset.at(10, 10), offset -> false);
        assertThat(index.index3x3Minimal()).isEqualTo(0);
    }

    @Test
    void createIndex_XXX3() {
        var index = AutoTile.createIndex(Offset.at(10, 10), offset -> true);
        assertThat(index.index3x3Minimal()).isEqualTo(255);
    }
}
