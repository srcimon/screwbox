package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.internal.AwtMapper;

import java.awt.*;

import static dev.screwbox.core.graphics.Color.BLACK;

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
        this(BLACK);
    }

    @Override
        target.drawImage(source, 0, 0, null);
        target.setColor(AwtMapper.toAwtColor(color.opacity(context.progress())));
        target.fillRect(0, 0, context.width(), context.height());
    }
}
