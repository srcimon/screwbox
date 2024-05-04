package io.github.srcimon.screwbox.core.scenes.animations;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.scenes.Animation;

public class ColorFadeAnimation implements Animation {

    private final Color color;

    public ColorFadeAnimation() {
        this(Color.BLACK);
    }

    public ColorFadeAnimation(final Color color) {
        this.color = color;
    }

    @Override
    public void draw(final Screen screen, final Percent progress) {
        screen.fillWith(color.opacity(progress));
    }
}
