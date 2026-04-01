package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

//TODO Add SceneTransitionBundle
/**
 * Fades the {@link Screen} to the specified {@link Color}.
 */
public class ColorFadeTransition implements TransitionAnimation {

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context, final Percent progress) {
        drawSourceImage(source, target, context);
        target.setColor(AwtMapper.toAwtColor(Color.BLACK.opacity(progress)));
        target.fillRect(context.bounds().x(), context.bounds().y(), context.bounds().width(), context.bounds().height());
    }
}
