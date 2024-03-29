package io.github.srcimon.screwbox.core.keyboard;

import io.github.srcimon.screwbox.core.utils.ListUtil;

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
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("combination must contain a key");
        }
        if (ListUtil.containsDuplicates(keys)) {
            throw new IllegalArgumentException("combination must not contain duplicate keys");
        }
        this.keys = keys;
    }

    /**
     * Returns all {@link Key}s in that {@link KeyCombination}.
     */
    public List<Key> keys() {
        return keys;
    }
}
