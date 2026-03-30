package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

import static java.awt.AlphaComposite.SRC_OVER;

/**
 * Fades the {@link Screen} to the specified {@link Sprite}.
 */
public record SpriteFadeTransition(Sprite sprite) implements TransitionAnimation {

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context, final Percent progress) {
        drawSourceImage(source, target, context);
        target.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) progress.value()));
        target.drawImage(sprite.image(Time.now()), 0, 0, null);
    }
}
