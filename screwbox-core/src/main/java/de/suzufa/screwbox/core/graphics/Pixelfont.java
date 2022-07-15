package de.suzufa.screwbox.core.graphics;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pixelfont {

    private final Map<Character, Sprite> characters = new HashMap<>();
    private Dimension characterSize;

    public static Pixelfont from(final List<Character> characters, final List<Sprite> sprites) {
        final Pixelfont pixelfont = new Pixelfont();

        return pixelfont;
    }

    public void addCharacter(final char character, final Sprite sprite) {
        requireNonNull(sprite, "Sprite must not be null. Character: " + character);

        if (!characters.isEmpty() && !sprite.dimension().equals(characterSize)) {
            throw new IllegalStateException("Pixelfont only supports uniform character size.");
        }
        if (hasCharacter(character)) {
            throw new IllegalStateException("Character already present in font: X");
        }

        characterSize = sprite.dimension();
        characters.put(character, sprite);
    }

    public Dimension characterSize() {
        if (isNull(characterSize)) {
            throw new IllegalStateException("Pixelfont has no characters.");
        }
        return characterSize;
    }

    public int characterCount() {
        return characters.size();
    }

    public boolean hasCharacter(final char character) {
        return characters.containsKey(character);
    }
}
