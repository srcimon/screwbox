package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.keyboard.DefaultKey;
import io.github.srcimon.screwbox.core.keyboard.Key;

public enum ControlKeys {

    @DefaultKey(Key.ESCAPE)
    QUIT,

    @DefaultKey(Key.ENTER)
    RESET,

    @DefaultKey(Key.ARROW_LEFT)
    LEFT,

    @DefaultKey(Key.ARROW_RIGHT)
    RIGHT
}
