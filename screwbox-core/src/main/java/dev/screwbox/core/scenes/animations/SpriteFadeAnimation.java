package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.awt.*;

import static java.awt.AlphaComposite.SRC_OVER;

/**
 * Fades the {@link Screen} to the specified {@link Sprite}.
 *
 * @since 3.26.0
 */
public record SpriteFadeAnimation(Sprite sprite) implements TransitionAnimation {

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        target.drawImage(source, 0, 0, null);
        target.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) progress.value()));
        target.drawImage(sprite.image(Time.now()), 0, 0, null);
    }
}
