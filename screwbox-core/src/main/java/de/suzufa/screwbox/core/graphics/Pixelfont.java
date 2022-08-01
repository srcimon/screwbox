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

    private static final long serialVersionUID = 1L;

    private final Map<Character, Sprite> characters = new HashMap<>();
    private int padding = 1;

    private static final Pixelfont DEFAULT_FONT = defaultFont("default_font.png");

    /**
     * A black monospace {@link Font}, containing a restricted set of characters,
     * numbers and symbols.
     */
    public static Pixelfont defaultFont() { // TODO: Add replaceColor-Function
        return DEFAULT_FONT;
    }

    // TODO: Test + javadoc
    public static Pixelfont defaultFont(Color color) { // TODO: Add replaceColor-Function
        var font = defaultFont("default_font.png");
        font.replaceColor(Color.BLACK, color);
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
    private static Pixelfont defaultFont(final String name) {
        final Pixelfont font = new Pixelfont();
        final var chracters = List.of(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.',
                ',', ':', '!', '?');
        final var sprites = Sprite.multipleFromFile(name, Dimension.of(7, 7), 1);

        font.addCharacters(chracters, sprites);
        return font;
    }

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

    private Sprite spriteFor(final char character) {
        final Sprite sprite = characters.get(character);
        if (nonNull(sprite)) {
            return sprite;
        }
        return characters.get(toUpperCase(character));
    }

}
