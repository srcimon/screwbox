package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SpriteTest {

    @Test
    void invisible_returnsInvisibleSprite() {
        Sprite sprite = Sprite.invisible();

        assertThat(sprite.dimension()).isEqualTo(Dimension.of(1, 1));
    }

    @Test
    void fromFile_imageFound_returnsSpriteFromFile() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        assertThat(sprite.dimension()).isEqualTo(Dimension.of(16, 16));
    }

    @Test
    void fromFile_fileNotFound_throwsEception() {
        assertThatThrownBy(() -> Sprite.fromFile("unknown.bmp"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown.bmp");
    }

    @Test
    void fromFile_notAnImage_throwsEception() {
        assertThatThrownBy(() -> Sprite.fromFile("test.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("image cannot be read: test.txt");
    }

    @Test
    void multipleFromFile_dimensionLargerThanSource_emptyResult() {
        Dimension tooLarge = Dimension.of(20, 20);

        assertThat(Sprite.multipleFromFile("tile.bmp", tooLarge)).isEmpty();
    }

    @Test
    void multipleFromFile_dimensionFitsSource_returnsSubImages() {
        Dimension dimension = Dimension.of(4, 2);

        assertThat(Sprite.multipleFromFile("tile.bmp", dimension))
                .hasSize(32)
                .allMatch(s -> s.dimension().equals(dimension));
    }

    @Test
    void multipleFromFile_withPadding_returnsSubImages() {
        Dimension dimension = Dimension.of(4, 2);

        assertThat(Sprite.multipleFromFile("tile.bmp", dimension, 4))
                .hasSize(6)
                .allMatch(s -> s.dimension().equals(dimension));
    }
}
