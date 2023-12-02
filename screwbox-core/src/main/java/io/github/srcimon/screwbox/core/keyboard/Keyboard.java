package io.github.srcimon.screwbox.core.keyboard;

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
}
