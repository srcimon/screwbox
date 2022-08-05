package de.suzufa.screwbox.core.graphics;

import static java.lang.Character.toUpperCase;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A font made of {@link Sprite}s (even animated ones) for system independent
 * rendering.
 * 
 * @see #defaultFont()
 * @see #defaultWhite()
 */
public class Pixelfont implements Serializable {

    private static final List<Character> DEFAULT_CHARACTER_SET = List.of(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.',
            ',', ':', '!', '?');

    private static final long serialVersionUID = 1L;

    private final Map<Character, Sprite> characters = new HashMap<>();
    private int padding = 1;

    /**
     * A monospace {@link Font}, containing a restricted set of characters, numbers
     * and symbols. Creating the font is quite costly.
     */
    public static Pixelfont defaultFont(final Color color) {
        final Pixelfont font = new Pixelfont();
        final var sprites = Sprite.multipleFromFile("default_font.png", Dimension.of(7, 7), 1);
        font.addCharacters(DEFAULT_CHARACTER_SET, sprites);
        font.replaceColor(Color.BLACK, requireNonNull(color, "Color must not be null."));
        return font;
    }

    /**
     * The space between characters of this font.
     */
    public int padding() {
        return padding;
    }

    /**
     * Sets the space between characters of this font.
     */
    public void setPadding(final int padding) {
        this.padding = padding;
    }

    // TODO: allow only same height sprites / drawTextCentered fixes TextHeight (see
    // helloWorld Demo)

    public void addCharacters(final List<Character> characters, final List<Sprite> sprites) {
        if (characters.size() != sprites.size()) {
            throw new IllegalArgumentException(
                    format("Count of characters (%d) is different than count of sprites (%d).",
                            characters.size(), sprites.size()));
        }
        for (int i = 0; i < characters.size(); i++) {
            addCharacter(characters.get(i), sprites.get(i));
        }
    }

    public void addCharacter(final char character, final Sprite sprite) {
        requireNonNull(sprite, "Sprite must not be null. Character: " + character);

        if (hasCharacter(character)) {
            throw new IllegalStateException("Character already present in font: X");
        }

        characters.put(character, sprite);
    }

    public int characterCount() {
        return characters.size();
    }

    public boolean hasCharacter(final char character) {
        return characters.containsKey(character);
    }

    public void replaceColor(final Color oldColor, final Color newColor) {
        for (var sprite : characters.values()) {
            sprite.replaceColor(oldColor, newColor);
        }
    }

    public List<Sprite> spritesFor(final String text) {
        final List<Sprite> sprites = new ArrayList<>();
        for (final var character : text.toCharArray()) {
            final var sprite = spriteFor(character);
            if (nonNull(sprite)) {
                sprites.add(sprite);
            }
        }
        return sprites;
    }

    // TODO: Tests and javadoc
    public Sprite spriteFor(final char character) {
        final Sprite sprite = characters.get(character);
        if (nonNull(sprite)) {
            return sprite;
        }
        return characters.get(toUpperCase(character));
    }

}
