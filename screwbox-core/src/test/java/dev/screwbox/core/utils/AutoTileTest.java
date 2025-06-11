package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AutoTileTest {

    //TODO ramp up tests and rename all
    @Test
    void createIndex_XXX() {
        var index = AutoTile.createIndex(Offset.at(10,10), offset -> offset.equals(Offset.at(11,10)));
        Assertions.assertThat(index.index3x3Minimal()).isEqualTo(4);
    }

    @Test
    void createIndex_XXX2() {
        var index = AutoTile.createIndex(Offset.at(10,10), offset -> false);
        Assertions.assertThat(index.index3x3Minimal()).isEqualTo(0);
    }

    @Test
    void createIndex_XXX3() {
        var index = AutoTile.createIndex(Offset.at(10,10), offset -> true);
        Assertions.assertThat(index.index3x3Minimal()).isEqualTo(255);
    }
}
