package dev.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AutoTileTest {

    AutoTile autoTile;

    @BeforeEach
    void setUp() {
        autoTile = AutoTile.fromSpriteSheet("assets/autotiles/rocks.png", AutoTile.Layout.LAYOUT_3X3);
    }

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

    @Test
    void spriteCount_using3x3Layout_is47() {
        assertThat(autoTile.spriteCount()).isEqualTo(47);
    }

    @RepeatedTest(4)
    void findSprite_anyMask_returnsASprite() {
        final var random = new Random();
        var sprite = autoTile.findSprite(AutoTile.createMask(Offset.at(10, 4), offset -> random.nextBoolean()));

        assertThat(sprite).isNotNull();
        assertThat(sprite.size()).isEqualTo(Size.square(16));
    }

    @Test
    void findSprite_noneConnected_returnsCorrectSprite() {
        var sprite = autoTile.findSprite(AutoTile.createMask(Offset.at(10, 4), offset -> false));

        verifyIsSameImage(sprite.singleImage(), "autotiles/none_connected.png");
    }

    @Test
    void findSprite_allConnected_returnsCorrectSprite() {
        var sprite = autoTile.findSprite(AutoTile.createMask(Offset.at(10, 4), offset -> true));

        verifyIsSameImage(sprite.singleImage(), "autotiles/all_connected.png");
    }

    @Test
    void size_returnsSpriteSize() {
        assertThat(autoTile.size()).isEqualTo(Size.square(16));
    }

}
