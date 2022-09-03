package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Sprite;

class SpriteDictionaryTest {

    private static final Sprite SPRITE = Sprite.fromFile("underworld.png");

    Tileset spriteDictionary;

    @BeforeEach
    void beforeEach() {
        spriteDictionary = new Tileset();
        spriteDictionary.addSprite(4, SPRITE);
        spriteDictionary.addNameToSprite(4, "underworld");
    }

    @Test
    void spriteCount_returnsSpriteCount() {
        assertThat(spriteDictionary.spriteCount()).isEqualTo(1);
    }

    @Test
    void findById_idPresent_returnsSprite() {
        assertThat(spriteDictionary.findById(4)).isEqualTo(SPRITE);
    }

    @Test
    void findById_idMissing_throwsException() {
        assertThatThrownBy(() -> spriteDictionary.findById(5))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("sprite not found: 5");
    }

    @Test
    void findByName_namePresent_returnsSprite() {
        assertThat(spriteDictionary.findByName("underworld")).isEqualTo(SPRITE);
    }

    @Test
    void findByName_nameMissing_throwsException() {
        assertThatThrownBy(() -> spriteDictionary.findByName("under"))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("sprite not found: under");
    }

    @Test
    void all_returnsAllSprites() {
        spriteDictionary.addSprite(9, SPRITE);

        assertThat(spriteDictionary.all()).hasSize(2);
    }
}
