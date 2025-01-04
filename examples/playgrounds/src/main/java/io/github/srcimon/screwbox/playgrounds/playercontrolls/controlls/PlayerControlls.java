package io.github.srcimon.screwbox.playgrounds.playercontrolls.controlls;

import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.KeyBinding;

public enum PlayerControlls implements KeyBinding {

    JUMP(Key.SPACE);

    private final Key key;

    PlayerControlls(final Key key) {
        this.key = key;
    }

    @Override
    public Key key() {
        return key;
    }
}
