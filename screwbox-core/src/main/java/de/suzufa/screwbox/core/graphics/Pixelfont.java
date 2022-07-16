package de.suzufa.screwbox.core.graphics;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pixelfont {

    private final Map<Character, Sprite> characters = new HashMap<>();

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

    // TODO: test and add substituations (upper-lowercase)
    public List<Sprite> spritesFor(final String text) {
        List<Sprite> sprites = new ArrayList<>();
        for (var character : text.toCharArray()) {
            // TODO: remove
            if (hasCharacter(character)) {
                sprites.add(characters.get(character));
            }
        }
        return sprites;
    }
}
