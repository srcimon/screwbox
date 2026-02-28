package dev.screwbox.core.graphics.filter;

import dev.screwbox.core.Percent;

import java.awt.*;
import java.awt.image.VolatileImage;

public class WarpPostFilter implements PostProcessingFilter {

    private final Percent strength;

    public WarpPostFilter(final Percent strength) {
        this.strength = strength;
    }
    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final PostProcessingContext context) {
        int w = source.getWidth();
        int h = source.getHeight();

        target.drawImage(source, 0, 0, null);

        for (int i = 1; i <= 3; i++) {
            double zoom = 1.0 + i * 0.05;
            double alpha = strength.value() / i;

            int nw = (int) (w * zoom);
            int nh = (int) (h * zoom);
            int dx = (w - nw) / 2;
            int dy = (h - nh) / 2;

            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            target.drawImage(source, dx, dy, nw, nh, null);
        }

        target.setComposite(AlphaComposite.SrcOver);

        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)};
        target.setPaint(new RadialGradientPaint(w / 2, h / 2, (float) (w * 0.7), dist, colors));
        target.fillRect(0, 0, w, h);
    }

}
