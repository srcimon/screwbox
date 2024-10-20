package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.scenes.Animation;

/**
 * Fades the {@link Screen} to the specified {@link Color}.
 */
public class ColorFadeAnimation implements Animation {

    private final Color color;

    /**
     * Fades to {@link Color#BLACK}.
     */
    public ColorFadeAnimation() {
        this(Color.BLACK);
    }

    /**
     * Fadeds to the specified {@link Color}.
     */
    public ColorFadeAnimation(final Color color) {
        this.color = color;
    }

    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        canvas.fillWith(color.opacity(progress));
    }
}
