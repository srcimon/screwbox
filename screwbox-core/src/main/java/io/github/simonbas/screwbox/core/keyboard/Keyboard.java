package io.github.simonbas.screwbox.core.keyboard;

public interface Keyboard {

    boolean isDown(Key key);

    boolean isDown(KeyCombination kombi);

    boolean justPressed(Key key);

    boolean anyKeyIsDown();
}
