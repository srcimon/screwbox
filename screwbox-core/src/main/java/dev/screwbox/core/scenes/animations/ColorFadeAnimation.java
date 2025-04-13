package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.scenes.Animation;

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
     * Fades to the specified {@link Color}.
     */
    public ColorFadeAnimation(final Color color) {
        this.color = color;
    }

    @Override
    public void draw(final Canvas canvas, final Percent progress) {
        canvas.fillWith(color.opacity(progress));
    }
}
