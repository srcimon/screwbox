package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * An modern looking animation shredding the scren into pieces.
 *
 * @since 3.26.0
 */
public record GridShredderAnimation(Size cellSize) implements TransitionAnimation {

    /**
     * Creates an instance with default cell size.
     */
    public GridShredderAnimation() {
        this(Size.of(64, 64));
    }

    public GridShredderAnimation {
        Validate.isTrue(cellSize::isValid, "cell size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int scaledWidth = (int)(cellSize.width() * context.resolutionScale());
        final int scaledHeight = (int)(cellSize.height() * context.resolutionScale());
        final int columns = (int) Math.ceil((double) context.width() / scaledWidth);
        final int rows = (int) Math.ceil((double) context.height() / scaledHeight);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                final double localProgress = context.progress().value();
                final double xPosition = x * (double) scaledWidth;
                final double yPosition = y * (double) scaledHeight;

                final double directionX = (y % 2 == 0) ? 1 : -1;
                final double directionY = (x % 2 == 0) ? 1 : -1;

                final double offsetX = Math.pow(localProgress, 2) * context.width() * directionX;
                final double offsetY = Math.pow(localProgress, 2) * context.height() * directionY;

                final var transform = new AffineTransform();
                transform.translate(xPosition + offsetX + scaledWidth / 2.0, yPosition + offsetY + scaledHeight / 2.0);
                transform.rotate(localProgress * Math.PI * 0.5 * directionX);
                transform.scale(1.0 - localProgress, 1.0 - localProgress);
                transform.translate(-xPosition - scaledWidth / 2.0, -yPosition - scaledHeight / 2.0);

                target.setClip(new Rectangle2D.Double(xPosition, yPosition, scaledWidth, scaledHeight));
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, (float) (1.0 - localProgress))));
                target.drawImage(source, transform, null);
            }
        }
    }
}

