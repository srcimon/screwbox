package io.github.srcimon.screwbox.core.keyboard;

import java.util.List;

public interface Keyboard {

    /**
     * Returns {@code true} if the given {@link Key} is down at the moment.
     *
     * @param key the {@link Key} to check
     */
    boolean isDown(Key key);

    /**
     * Returns {@code true} if the given {@link KeyCombination} is down at the moment.
     *
     * @param combination the {@link KeyCombination} to check
     */
    boolean isDown(KeyCombination combination);

    /**
     * Returns {@code true} if any key is down at the moment.
     */
    boolean isAnyKeyDown();

    /**
     * Returns {@code true} if the given {@link Key} was typed right now.
     *
     * @param key the {@link Key} to check
     */
    boolean isPressed(Key key);

    /**
     * Returns all pressed {@link Key}s.
     */
    List<Key> pressedKeys();

    /**
     * Returns all currently down {@link Key}s.
     */
    List<Key> downKeys();
}
