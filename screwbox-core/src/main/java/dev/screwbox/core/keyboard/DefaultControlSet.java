package dev.screwbox.core.keyboard;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;

/**
 * Default control set used for controlling the movement of an {@link Entity} using the {@link Keyboard}.
 *
 * @see JumpControlComponent
 * @see LeftRightControlComponent
 *
 * @since 2.15.0
 */
public enum DefaultControlSet {

    @DefaultKey(Key.ARROW_LEFT)
    LEFT,

    @DefaultKey(Key.ARROW_RIGHT)
    RIGHT,

    @DefaultKey(Key.SPACE)
    JUMP
}
