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
        final var area = context.bounds();
        target.drawImage(source, area.x(), area.y(), area.maxX(), area.maxY(), area.x(), area.y(), area.maxX(), area.maxY(), null);
        target.setStroke(new BasicStroke(2));
        for (int y = area.y(); y < area.height(); y += 4) {
            final var alpha = MathUtil.fastSin((seed + y * 20) / 600.0) / 10.0 + 0.25;
            target.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) alpha));
            target.drawLine(area.x(), y, area.maxX(), y);
        }
        target.setComposite(AlphaComposite.getInstance(SRC_OVER, 1f));
        final var image = EDGE.get().singleImage();
        final var size = EDGE.get().size();
        if (area.size().width() > size.width() && area.size().height() > size.height()) {
            target.drawImage(image, area.x(), area.y(), null);
            target.drawImage(image, area.maxX(), area.y(), area.maxX() - size.width(), area.y() + size.height(), 0, 0, size.width(), size.height(), null);
            target.drawImage(image, area.x(), area.maxY(), area.x() + size.width(), area.maxY() - size.height(), 0, 0, size.width(), size.height(), null);
            target.drawImage(image, area.maxX(), area.maxY(), area.maxX() - size.width(), area.maxY() - size.height(), 0, 0, size.width(), size.height(), null);
        }
    }
}
