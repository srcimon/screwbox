package dev.screwbox.core.scenes;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Screen;

/**
 * An animation used to leave a {@link Scene}.
 */
@FunctionalInterface
public interface Animation {

    /**
     * Draw on the {@link Screen} dependent of the leaving or entering progress.
     * On entering progress will count down not up to automatically reverse the animation.
     */
    void draw(Canvas canvas, Percent progress);
}
