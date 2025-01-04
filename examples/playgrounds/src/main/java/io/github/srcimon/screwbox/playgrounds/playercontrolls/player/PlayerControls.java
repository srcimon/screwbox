package io.github.srcimon.screwbox.playgrounds.playercontrolls.player;

import io.github.srcimon.screwbox.core.keyboard.DefaultKey;
import io.github.srcimon.screwbox.core.keyboard.Key;

public enum PlayerControls {

    @DefaultKey(Key.SPACE)
    JUMP,

    @DefaultKey(Key.Q)
    DASH,

    @DefaultKey(Key.ARROW_LEFT)
    LEFT,

    @DefaultKey(Key.ARROW_RIGHT)
    RIGHT,

    @DefaultKey(Key.ARROW_UP)
    UP,

    @DefaultKey(Key.ARROW_DOWN)
    DOWN,

    @DefaultKey(Key.ENTER)
    RESET,

    @DefaultKey(Key.SHIFT_LEFT)
    GRAB
}
