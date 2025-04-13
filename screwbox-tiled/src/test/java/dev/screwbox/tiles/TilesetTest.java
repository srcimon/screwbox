package dev.screwbox.tiles;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TilesetTest {

    private static final Sprite SPRITE = Sprite.fromFile("underworld.png");

    Tileset tileset;

    @BeforeEach
    void beforeEach() {
        tileset = new Tileset(emptyList());
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

    @Test
    void single_onlyOneSprite_returnsSprite() {
        Sprite sprite = tileset.single();

        assertThat(sprite).isEqualTo(SPRITE);
    }

    @Test
    void single_twoSprites_throwsException() {
        tileset.addSprite(9, SPRITE);

        assertThatThrownBy(() -> tileset.single())
                .isInstanceOf(IllegalStateException.class).hasMessage("tileset has not exactly one sprite");
    }

    @Test
    void first_noSprites_throwsException() {
        tileset.clear();

        assertThatThrownBy(() -> tileset.first())
                .isInstanceOf(IllegalStateException.class).hasMessage("tileset has no sprite");
    }

    @Test
    void first_hasSprites_returnsFirst() {
        tileset.addSprite(9, SPRITE);
        tileset.addSprite(11, SPRITE.freshInstance());

        assertThat(tileset.first()).isEqualTo(SPRITE);
    }

    @Test
    void clear_hasSprites_isEmpty() {
        tileset.clear();

        assertThat(tileset.spriteCount()).isZero();
        assertThat(tileset.all()).isEmpty();
    }

    @Test
    void spriteFromJson_noName_returnsSprite() {
        Sprite sprite = Tileset.spriteFromJson("underworld.json");

        assertThat(sprite).isNotNull();
    }

    @Test
    void spriteFromJson_spriteFound_returnsSprite() {
        Sprite sprite = Tileset.spriteFromJson("underworld.json", "myNamedSprite");

        assertThat(sprite).isNotNull();
    }

    @Test
    void spriteAssetFromJson_noName_returnsSprite() {
        Asset<Sprite> sprite = Tileset.spriteAssetFromJson("underworld.json");

        assertThat(sprite.get()).isNotNull();
    }

    @Test
    void spriteAssetFromJson_spriteFound_returnsSprite() {
        Asset<Sprite> sprite = Tileset.spriteAssetFromJson("underworld.json", "myNamedSprite");

        assertThat(sprite.get()).isNotNull();
    }
}