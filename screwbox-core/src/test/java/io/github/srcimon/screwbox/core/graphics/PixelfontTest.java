package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.assets.FontBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PixelfontTest {

    private Pixelfont pixelfont;

    @BeforeEach
    void beforeEach() {
        pixelfont = new Pixelfont();
    }

    @Test
    void characterCount_hasCharacters_returnsCount() {
        pixelfont.addCharacter('A', Sprite.invisible());
        pixelfont.addCharacter('B', Sprite.invisible());

        assertThat(pixelfont.characterCount()).isEqualTo(2);
    }

    @Test
    void addCharacter_spriteNull_throwsException() {
        assertThatThrownBy(() -> pixelfont.addCharacter('A', null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Sprite must not be null. Character: A");
    }

    @Test
    void addCharacter_newCharacter_characterIsAdded() {
        pixelfont.addCharacter('X', Sprite.invisible());

        assertThat(pixelfont.hasCharacter('X')).isTrue();
    }

    @Test
    void addCharacter_differentHeight_throwsException() {
        pixelfont.addCharacter('A', Sprite.invisible());

        var sprite = Sprite.fromFile("tile.bmp");

        assertThatThrownBy(() -> pixelfont.addCharacter('B', sprite))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New character has different height than pixelfont.");
    }

    @Test
    void height_noSprite_returnsZero() {
        assertThat(pixelfont.height()).isZero();
    }

    @Test
    void height_withSprite_returnsSpriteHeight() {
        pixelfont.addCharacter('A', Sprite.fromFile("tile.bmp"));

        assertThat(pixelfont.height()).isEqualTo(16);
    }

    @Test
    void addCharacter_characterAlreadyPresent_throwsException() {
        Sprite sprite = Sprite.invisible();

        pixelfont.addCharacter('X', sprite);

        assertThatThrownBy(() -> pixelfont.addCharacter('X', sprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Character already present in font: X");
    }

    @Test
    void addCharacters_sameAmoutOfCharactersAndSprites_addsAll() {
        Sprite sprite = Sprite.invisible();

        List<Character> characters = List.of('A', 'B', 'C');
        List<Sprite> sprites = List.of(sprite, sprite, sprite);

        pixelfont.addCharacters(characters, sprites);

        assertThat(pixelfont.hasCharacter('A')).isTrue();
        assertThat(pixelfont.hasCharacter('B')).isTrue();
        assertThat(pixelfont.hasCharacter('C')).isTrue();
        assertThat(pixelfont.characterCount()).isEqualTo(3);
    }

    @Test
    void addCharacters_moreCharactersThanSprites_throwsException() {
        List<Character> characters = List.of('A', 'B', 'C');
        List<Sprite> sprites = List.of(Sprite.invisible());

        assertThatThrownBy(() -> pixelfont.addCharacters(characters, sprites))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Count of characters (3) is different than count of sprites (1).");
    }

    @Test
    void spritesFor_textContainsOnlyUnknownCharacters_isEmpty() {
        var font = FontBundle.BOLDZILLA.get();

        assertThat(font.spritesFor("@@@@")).isEmpty();
    }

    @Test
    void spritesFor_textContainsOnlyKnownCharacters_returnsSprites() {
        var font = FontBundle.BOLDZILLA.get();

        assertThat(font.spritesFor("HELLO")).hasSize(5);
    }

    @Test
    void spritesFor_textHasLowercaseCharacters_returnsSpritesFromUppercaseCharacters() {
        var font = FontBundle.BOLDZILLA.get();

        assertThat(font.spritesFor("Hello")).hasSize(5);
    }

    @Test
    void spriteFor_characterPresent_returnsSprite() {
        pixelfont.addCharacter('A', Sprite.invisible());

        assertThat(pixelfont.spriteFor('A')).isNotNull();
        assertThat(pixelfont.spriteFor('a')).isNotNull();
    }

    @Test
    void spriteFor_justHasLowerCaseVersionofCharacter_returnsSprite() {
        pixelfont.addCharacter('a', Sprite.invisible());

        assertThat(pixelfont.spriteFor('a')).isNotNull();
        assertThat(pixelfont.spriteFor('A')).isNotNull();
    }

    @Test
    void spriteFor_characterMissing_returnsNull() {
        pixelfont.addCharacter('A', Sprite.invisible());

        assertThat(pixelfont.spriteFor('B')).isNull();
    }
}
