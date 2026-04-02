package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Washes down the current screen like it was wet paint on a wall. Only suitable as outro animation.
 *
 * @since 3.26.0
 */
//TODO apply resoultionscale
public record WashDownAnimation(int stripeWidth) implements TransitionAnimation {

    /**
     * Creates an instance with default stripe width.
     */
    public WashDownAnimation() {
        this(2);
    }

    public WashDownAnimation {
        Validate.range(stripeWidth, 1, 32, "stripe width must be in range 1 to 32");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        for (int x = 0; x < context.width(); x += stripeWidth) {
            final double variation = Math.sin(x * 0.05) * Math.cos(x * 0.02) * 0.5 + 0.5;
            final double localProgress = Math.max(0, (context.progress().value() - variation * 0.3) / 0.7);

            if (localProgress < 1.0) {
                final int offsetY = (int) (Math.pow(localProgress, 2) * context.height());
                final int offsetX = (int) (Math.sin(localProgress * 10 + x) * 5 * localProgress);
                target.setClip(x, 0, stripeWidth, context.height());
                target.drawImage(source, offsetX, offsetY, null);
            }
        }
    }
}
