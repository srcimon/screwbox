package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.AwtMapper;

import java.awt.*;

/**
 * Fades the {@link Screen} to the specified {@link Color}.
 *
 * @since 3.26.0
 */
public record ColorFadeAnimation(Color color) implements TransitionAnimation {

    /**
     * Fades to {@link Color#BLACK}.
     */
    public ColorFadeAnimation() {
        this(Color.BLACK);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        target.drawImage(source, 0, 0, null);
        target.setColor(AwtMapper.toAwtColor(color.opacity(progress)));
        target.fillRect(0, 0, size.width(), size.height());
    }
}
