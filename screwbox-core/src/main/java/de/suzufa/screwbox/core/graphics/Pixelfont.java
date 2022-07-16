package de.suzufa.screwbox.core.graphics;

import static java.lang.Character.toUpperCase;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pixelfont {

    private final Map<Character, Sprite> characters = new HashMap<>();

    private static final Pixelfont DEFAULT_WHITE = defaultFont("default_font_white");
    private static final Pixelfont DEFAULT_BLACK = defaultFont("default_font_black");

    public static Pixelfont defaultWhite() {
        return DEFAULT_WHITE;
    }

    public static Pixelfont defaultBlack() {
        return DEFAULT_BLACK;
    }

    private static Pixelfont defaultFont(final String name) {
        final Pixelfont font = new Pixelfont();
        font.addCharacters(
                List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.',
                        ',', ':', '!', '?'),
                Sprite.multipleFromFile("default_font_white.png", Dimension.of(7, 7), 1));
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
