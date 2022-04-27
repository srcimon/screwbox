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

}
