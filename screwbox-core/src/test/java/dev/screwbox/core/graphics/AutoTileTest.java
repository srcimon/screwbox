package dev.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AutoTileTest {

    @Test
    void createMask_tileOffsetNull_throwsException() {
        assertThatThrownBy(() -> AutoTile.createMask(null, offset -> true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("tile offset must not be null");
    }

    @Test
    void index3x3_northNeighbourIsConnected_isFour() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> offset.equals(Offset.at(11, 10)));
        assertThat(mask.index3x3()).isEqualTo(4);
    }

    @Test
    void index3x3_noNeighbourIsConnected_isZero() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> false);

        assertThat(mask.index3x3()).isZero();
    }

    @Test
    void index3x3_allNeighboursAreConnected_isMaxIndex() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> true);

        assertThat(mask.index3x3()).isEqualTo(255);
    }

    @Test
    void hasToString_allSet_contains2x2And3x3Indices() {
        var mask = AutoTile.createMask(Offset.at(10, 10), offset -> true);

        assertThat(mask).hasToString("Mask[2x2:15 / 3x3:255]");
    }

    @Test
    void fromSpriteSheet_imageDoesntMatchLayout_throwsException() {
        assertThatThrownBy(() -> AutoTile.fromSpriteSheet("assets/autotiles/rocks.png", AutoTile.Layout.LAYOUT_2X2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("aspect ratio of image (192:64) doesn't match template (1:1)");
    }

    @Test
    void fromSpriteSheet_templateNull_throwsException() {
        assertThatThrownBy(() -> AutoTile.fromSpriteSheet("assets/autotiles/rocks.png", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("template must not be null");
    }

}
