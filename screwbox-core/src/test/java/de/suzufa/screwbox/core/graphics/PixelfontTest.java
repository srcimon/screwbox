package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PixelfontTest {

    private Pixelfont pixelfont;

    @BeforeEach
    void beforeEach() {
        pixelfont = new Pixelfont();
    }

    @Test
    void characterSize_noCharacters_throwsException() {
        assertThatThrownBy(() -> pixelfont.characterSize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pixelfont has no characters.");
    }

    @Test
    void characterSize_hasCharacter_returnsCharacterSize() {
        pixelfont.addCharacter('A', Sprite.fromFile("tile.bmp"));

        assertThat(pixelfont.characterSize()).isEqualTo(Dimension.of(16, 16));
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
    void addCharacter_characterHasDifferentSize_throwsException() {
        Sprite differentSize = Sprite.fromFile("tile.bmp");

        pixelfont.addCharacter('X', Sprite.invisible());

        assertThatThrownBy(() -> pixelfont.addCharacter('A', differentSize))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pixelfont only supports uniform character size.");
    }

    @Test
    void addCharacter_characterAlreadyPresent_throwsException() {
        Sprite sprite = Sprite.invisible();

        pixelfont.addCharacter('X', sprite);

        assertThatThrownBy(() -> pixelfont.addCharacter('X', sprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Character already present in font: X");
    }
}
