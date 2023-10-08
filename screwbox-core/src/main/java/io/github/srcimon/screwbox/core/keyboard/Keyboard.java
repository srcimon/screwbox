package io.github.srcimon.screwbox.core.keyboard;

public interface Keyboard {

    boolean isDown(Key key);

    boolean isDown(KeyCombination kombi);

    boolean justPressed(Key key);

    /**
     * Returns true if any key is down at the moment.
     */
    boolean isAnyKeyDown();
}
