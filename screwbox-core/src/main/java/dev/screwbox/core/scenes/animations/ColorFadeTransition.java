package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.AwtMapper;

import java.awt.*;

//TODO Add SceneTransitionBundle

/**
 * Fades the {@link Screen} to the specified {@link Color}.
 */
public class ColorFadeTransition implements TransitionAnimation {

    @Override
    public void apply(final Image source, final Graphics2D target, final ScreenBounds bounds, final Percent progress) {
        target.drawImage(source, 0, 0, null);
        target.setColor(AwtMapper.toAwtColor(Color.BLACK.opacity(progress)));
        target.fillRect(bounds.x(), bounds.y(), bounds.width(), bounds.height());
    }
}
