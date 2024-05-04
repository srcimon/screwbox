package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

/**
 * An animation used to leave a {@link Scene}.
 */
public interface Animation {

    /**
     * Draw on the {@link Screen} dependent of the leaving or entering progress. On Entering progress will count down not
     * up.
     */
    void draw(Screen screen, Percent progress);
}
