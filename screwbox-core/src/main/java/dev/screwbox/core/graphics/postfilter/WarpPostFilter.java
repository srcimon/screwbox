package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Percent;

import java.awt.*;

public class WarpPostFilter implements PostProcessingFilter {

    private static final float[] DIST = {0.0f, 1.0f};
    private static final Color[] COLORS = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)};

    private final Percent strength;

    public WarpPostFilter(final Percent strength) {
        this.strength = strength;
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);
        for (int i = 1; i <= 3; i++) {
            double zoom = 1.0 + i * 0.05;
            double alpha = strength.value() / i;

            // Ziel-Dimensionen (skaliert)
            int nw = (int) (area.width() * zoom);
            int nh = (int) (area.height() * zoom);

            // Ziel-Offsets (Zentrierung des Zooms relativ zur Originalposition)
            // Wir addieren xP und yP, damit das Bild an der ursprünglichen Subimage-Position bleibt
            int dx1 = area.x() + (area.width() - nw) / 2;
            int dy1 = area.y() + (area.height() - nh) / 2;
            int dx2 = dx1 + nw;
            int dy2 = dy1 + nh;

            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));

            target.drawImage(source, dx1, dy1, dx2, dy2, area.x(), area.y(), area.maxX(), area.maxY(), null);
        }

        target.setComposite(AlphaComposite.SrcOver);
        target.setPaint(new RadialGradientPaint(area.x() + area.width() / 2f, area.y() + area.height() / 2f, (float) (area.width() * 0.7), DIST, COLORS));
        target.fillRect(area.x(), area.y(), area.width(), area.height());
    }

}
