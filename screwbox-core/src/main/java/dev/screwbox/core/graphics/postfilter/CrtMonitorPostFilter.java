package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.utils.MathUtil;

import java.awt.*;

import static java.awt.AlphaComposite.SRC_OVER;

/**
 * Adds a CRT Monitor overlay on the screen.
 *
 * @since 3.25.0
 */
public class CrtMonitorPostFilter implements PostProcessingFilter {

    private static final Asset<Sprite> EDGE = SpriteBundle.CRT_MONITOR_EDGE.asset();

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final long seed = context.lifetime().milliseconds();
        final var bounds = context.bounds();
        drawSourceImage(source, target, context);
        target.setStroke(new BasicStroke((int)(2 * context.resolutionScale())));
        for (int y = bounds.y(); y < bounds.height(); y += (int)(4 * context.resolutionScale())) {
            final var alpha = MathUtil.fastSin((seed + y * 20) / 600.0) / 10.0 + 0.25;
            target.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) alpha));
            target.drawLine(bounds.x(), y, bounds.maxX(), y);
        }
        target.setComposite(AlphaComposite.getInstance(SRC_OVER, 1f));
        final var image = EDGE.get().singleImage();
        final var size = EDGE.get().size();
        if (bounds.width() > size.width() && bounds.height() > size.height()) {
            target.drawImage(image, bounds.x(), bounds.y(), null);
            target.drawImage(image, bounds.maxX(), bounds.y(), bounds.maxX() - size.width(), bounds.y() + size.height(), 0, 0, size.width(), size.height(), null);
            target.drawImage(image, bounds.x(), bounds.maxY(), bounds.x() + size.width(), bounds.maxY() - size.height(), 0, 0, size.width(), size.height(), null);
            target.drawImage(image, bounds.maxX(), bounds.maxY(), bounds.maxX() - size.width(), bounds.maxY() - size.height(), 0, 0, size.width(), size.height(), null);
        }
    }
}
