package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Percent;

import java.awt.*;

/**
 * Applies a warp effect on the screen.
 *
 * @since 3.24.0
 */
public record WarpPostFilter(Percent strength) implements PostProcessingFilter {

    private static final float[] DIST = {0.0f, 1.0f};
    private static final Color[] COLORS = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)};

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);

        for (int i = 1; i <= 3; i++) {
            final double zoom = 1.0 + i * 0.05;
            final double alpha = strength.value() / i;

            final int nw = (int) (area.width() * zoom);
            final int nh = (int) (area.height() * zoom);

            final int dx1 = area.x() + (area.width() - nw) / 2;
            final int dy1 = area.y() + (area.height() - nh) / 2;
            final int dx2 = dx1 + nw;
            final int dy2 = dy1 + nh;

            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));

            target.drawImage(source, dx1, dy1, dx2, dy2, area.x(), area.y(), area.maxX(), area.maxY(), null);
        }

        target.setComposite(AlphaComposite.SrcOver);
        target.setPaint(new RadialGradientPaint(area.x() + area.width() / 2f, area.y() + area.height() / 2f, (float) (area.width() * 0.7), DIST, COLORS));
        target.fillRect(area.x(), area.y(), area.width(), area.height());
    }

}
