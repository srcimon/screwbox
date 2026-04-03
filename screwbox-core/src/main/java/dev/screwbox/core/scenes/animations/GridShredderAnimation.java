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
//TODO apply resoultionscale
public record GridShredderAnimation(Size gridSize) implements TransitionAnimation {

    /**
     * Creates an instance with default grid size.
     */
    public GridShredderAnimation() {
        this(Size.of(12, 10));
    }

    public GridShredderAnimation {
        Validate.isTrue(gridSize::isValid, "grid size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final double tileWidth = (double) context.width() / gridSize.width();
        final double tileHeight = (double) context.height() / gridSize.height();

        for (int y = 0; y < gridSize.height(); y++) {
            for (int x = 0; x < gridSize.width(); x++) {
                final double delay = (double) (x + y) / (gridSize.width() + gridSize.height());
                final double localPosition = Math.clamp((context.progress().value() - delay * 0.4) / 0.6, 0, 1);

                if (localPosition < 1.0) {
                    final double xPos = x * tileWidth;
                    final double yPos = y * tileHeight;

                    final double directionX = (y % 2 == 0) ? 1 : -1;
                    final double directionY = (x % 2 == 0) ? 1 : -1;

                    final double offsetX = Math.pow(localPosition, 2) * context.width() * directionX;
                    final double offsetY = Math.pow(localPosition, 2) * context.height() * directionY;

                    final AffineTransform transform = new AffineTransform();
                    transform.translate(xPos + offsetX + tileWidth / 2, yPos + offsetY + tileHeight / 2);
                    transform.rotate(localPosition * Math.PI * 0.5 * directionX);
                    transform.scale(1.0 - localPosition, 1.0 - localPosition);
                    transform.translate(-xPos - tileWidth / 2, -yPos - tileHeight / 2);
                    target.setClip(new Rectangle2D.Double(xPos, yPos, tileWidth, tileHeight));
                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, (float) (1.0 - localPosition))));
                    target.drawImage(source, transform, null);
                }
            }
        }
    }
}

