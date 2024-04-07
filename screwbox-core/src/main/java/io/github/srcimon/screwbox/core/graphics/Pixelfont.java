package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.assets.FontsBundle;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * A font made of {@link Sprite}s (even animated ones) for system independent rendering. Have a look at {@link FontsBundle}
 * if you want some inspiration.
 *
 * @see FontsBundle
 */
public class Pixelfont implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<Character, Sprite> characters = new HashMap<>();
    private int padding = 2;
    private int height = 0;

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

    /**
     * Returns the width of a given text in this {@link Pixelfont}.  Will be 0 if there is no character jet.
     *
     * @see #sizeOf(String)
     */
    public int widthOf(final String text) {
        int totalWith = 0;
        for (final var sprite : spritesFor(text)) {
            totalWith += sprite.size().width();
            totalWith += padding();
        }

        return totalWith;
    }

    /**
     * Returns the {@link Size} of a given text in this {@link Pixelfont}. Width will be 0 if there is no character jet.
     *
     * @see #widthOf(String)
     */
    public Size sizeOf(final String text) {
        return Size.of(widthOf(text), height);
    }
//TODO remove size methods / replace with options?
    //TODO remove padding
    /**
     * Replaces the {@link Color} in all {@link Frame}s of all {@link Sprite}s of the {@link Pixelfont}.
     */
    public Pixelfont replaceColor(final Color originalColor, final Color newColor) {
        final Pixelfont newFont = new Pixelfont();

        for (final var character : characters.entrySet()) {
            final Sprite recoloredSprite = originalColor.equals(newColor)
                    ? character.getValue()
                    : character.getValue().replaceColor(originalColor, newColor);
            newFont.addCharacter(character.getKey(), recoloredSprite);
        }
        newFont.setPadding(padding);
        return newFont;
    }
}
