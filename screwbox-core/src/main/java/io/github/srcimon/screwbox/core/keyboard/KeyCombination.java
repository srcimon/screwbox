package io.github.srcimon.screwbox.core.keyboard;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.List;

/**
 * A combination of multiple {@link Key}s.
 */
public class KeyCombination {

    private final List<Key> keys;

    /**
     * Creats a new instance of the given {@link Key}s. Must have at leas one key.
     */
    public static KeyCombination ofKeys(final Key... keys) {
        return new KeyCombination(List.of(keys));
    }

    private KeyCombination(final List<Key> keys) {
        Validate.notEmpty(keys, "combination must contain a key");
        Validate.noDuplicates(keys, "combination must not contain duplicate keys");
        this.keys = keys;
    }

    /**
     * Returns all {@link Key}s in that {@link KeyCombination}.
     */
    public List<Key> keys() {
        return keys;
    }
}
