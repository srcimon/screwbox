package de.suzufa.screwbox.core.keyboard;

import java.util.Collection;
import java.util.List;

public class KeyCombination {

    private final Collection<Key> keys;

    public static KeyCombination ofKeys(final Key... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("combination must contain a key");
        }
        return new KeyCombination(List.of(keys));
    }

    private KeyCombination(final List<Key> keys) {
        this.keys = keys;
    }

    public Collection<Key> keys() {
        return keys;
    }
}
