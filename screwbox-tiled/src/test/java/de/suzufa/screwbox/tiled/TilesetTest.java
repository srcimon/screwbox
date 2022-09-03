package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Sprite;

class TilesetTest {

    private static final Sprite SPRITE = Sprite.fromFile("underworld.png");

    Tileset tileset;

    @BeforeEach
    void beforeEach() {
        tileset = new Tileset();
        tileset.addSprite(4, SPRITE);
        tileset.addNameToSprite(4, "underworld");
    }

    @Test
    void spriteCount_returnsSpriteCount() {
        assertThat(tileset.spriteCount()).isEqualTo(1);
    }

    @Test
    void findById_idPresent_returnsSprite() {
        assertThat(tileset.findById(4)).isEqualTo(SPRITE);
    }

    @Test
    void findById_idMissing_throwsException() {
        assertThatThrownBy(() -> tileset.findById(5))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("sprite not found: 5");
    }

    @Test
    void findByName_namePresent_returnsSprite() {
        assertThat(tileset.findByName("underworld")).isEqualTo(SPRITE);
    }

    @Test
    void findByName_nameMissing_throwsException() {
        assertThatThrownBy(() -> tileset.findByName("under"))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("sprite not found: under");
    }

    @Test
    void all_returnsAllSprites() {
        tileset.addSprite(9, SPRITE);

        assertThat(tileset.all()).hasSize(2);
    }
}
