package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AutoTileTest {

    //TODO ramp up tests and rename all
    @Test
    void createMask_At_ForOffset_XXX() {
        var index = AutoTile.createMask(Offset.at(10, 10), offset -> offset.equals(Offset.at(11, 10)));
        assertThat(index.mask3x3()).isEqualTo(4);
    }

    @Test
    void createMask_At_ForOffset_XXX2() {
        var index = AutoTile.createMask(Offset.at(10, 10), offset -> false);
        assertThat(index.mask3x3()).isEqualTo(0);
    }

    @Test
    void createMask_At_ForOffset_XXX3() {
        var index = AutoTile.createMask(Offset.at(10, 10), offset -> true);
        assertThat(index.mask3x3()).isEqualTo(255);
    }
}
