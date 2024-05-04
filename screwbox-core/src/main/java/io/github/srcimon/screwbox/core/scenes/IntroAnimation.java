package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * An animation used to enter a {@link Scene}.
 */
public interface IntroAnimation {

    /**
     * Draw on the {@link Screen} dependent of the entering value. A screenshot of the last {@link Scene} can be used.
     */
    void draw(Screen screen, Percent value, Sprite screenshot);
}
