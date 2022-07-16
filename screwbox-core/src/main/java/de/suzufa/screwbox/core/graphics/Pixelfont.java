package de.suzufa.screwbox.core.graphics;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pixelfont {

    private final Map<Character, Sprite> characters = new HashMap<>();
    private Dimension characterSize;

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
