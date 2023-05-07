package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.utils.Cache;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.*;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * A font made of {@link Sprite}s (even animated ones) for system independent
 * rendering.
 *
 * @see #defaultFont(Color)
 */
public class Pixelfont implements Serializable {

    private static final List<Character> DEFAULT_CHARACTER_SET = List.of(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.',
            ',', ':', '!', '?', '-');
    private static final Pixelfont DEFAULT_FONT = defaultFontBlack();

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<Character, Sprite> characters = new HashMap<>();
    private static final Cache<Color, Pixelfont> FONT_CACHE = new Cache<>();
    private int padding = 2;
    private int height = 0;

    /**
     * Creates a monospace {@link Font}, containing a restricted set of characters,
     * numbers and symbols. First call for every {@link Color} is quite slow.
     */
    public static Pixelfont defaultFont(final Color color) {
        final Color newColor = requireNonNull(color, "Color must not be null.");
        return FONT_CACHE.getOrElse(newColor, () -> DEFAULT_FONT.replaceBlack(newColor));
    }

    private static Pixelfont defaultFontBlack() {
        final Pixelfont font = new Pixelfont();
        final var sprites = Sprite.multipleFromFile("default_font.png", Dimension.square(8));
        font.addCharacters(DEFAULT_CHARACTER_SET, sprites);
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
        if (padding <= 0) {
            throw new IllegalArgumentException("Padding must have positive value");
        }
        this.padding = padding;
    }

    /**
     * Adds new characters with corresponding {@link Sprite}s to the
     * {@link Pixelfont}. Both lists must have the same size.
     *
     * @see #addCharacter(char, Sprite)
     */
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

    /**
     * Adds a new character and the corresponding {@link Sprite} to the
     * {@link Pixelfont}.
     *
     * @see #addCharacters(List, List)
     */
    public void addCharacter(final char character, final Sprite sprite) {
        requireNonNull(sprite, "Sprite must not be null. Character: " + character);

        if (hasCharacter(character)) {
            throw new IllegalStateException("Character already present in font: X");
        }

        if (height == 0) {
            height = sprite.size().height();
        } else {
            if (height != sprite.size().height()) {
                throw new IllegalArgumentException("New character has different height than pixelfont.");
            }
        }

        characters.put(character, isSpaceChar(character) ? sprite : sprite.cropHorizontal());
    }

    /**
     * Returns the current count of different characters in the {@link Pixelfont}.
     */
    public int characterCount() {
        return characters.size();
    }

    /**
     * Checks if the given character is contained in the {@link Pixelfont}.
     */
    public boolean hasCharacter(final char character) {
        return characters.containsKey(character);
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

    /**
     * Returns the sprite for the given {@link Character}. Ignores case of
     * {@link Character} if character is missing. Returns null if there is no such
     * {@link Character}.
     */
    public Sprite spriteFor(final char character) {
        final Sprite sprite = characters.get(character);
        if (nonNull(sprite)) {
            return sprite;
        }
        return isUpperCase(character)
                ? characters.get(toLowerCase(character))
                : characters.get(toUpperCase(character));
    }

    /**
     * Returns the height of the pixelfont. Every {@link Sprite} in the font has the
     * same height. Will be 0 if there is no character jet.
     */
    public int height() {
        return height;
    }

    private Pixelfont replaceBlack(final Color newColor) {
        final Pixelfont newFont = new Pixelfont();

        for (final var character : characters.entrySet()) {
            final Sprite recoloredSprite = character.getValue().replaceColor(Color.BLACK, newColor);
            newFont.addCharacter(character.getKey(), recoloredSprite);
        }
        newFont.setPadding(padding);
        return newFont;
    }
}
