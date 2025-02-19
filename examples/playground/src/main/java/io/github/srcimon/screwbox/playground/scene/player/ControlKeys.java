package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.keyboard.DefaultKey;
import io.github.srcimon.screwbox.core.keyboard.Key;

public enum ControlKeys {

    @DefaultKey(Key.ESCAPE)
    QUIT,

    @DefaultKey(Key.ENTER)
    RESET,

    @DefaultKey(Key.V)
    JUMP,

    @DefaultKey(Key.C)
    GRAB,

    @DefaultKey(Key.ARROW_UP)
    UP,

    @DefaultKey(Key.ARROW_DOWN)
    DOWN,

    @DefaultKey(Key.ARROW_LEFT)
    LEFT,

    @DefaultKey(Key.X)
    DASH,

    @DefaultKey(Key.ARROW_RIGHT)
    RIGHT
}
