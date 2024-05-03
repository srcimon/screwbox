package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

/**
 * An animation used to leave a {@link Scene}.
 */
public interface ExtroAnimation {

    /**
     * Draw on the {@link Screen} dependent of the leaving value.
     */
    void draw(Screen screen, Percent value);
}
