package dev.screwbox.core.graphics.postfilter;

import java.awt.*;
import java.awt.image.VolatileImage;

public class UnderwaterPostFilter implements PostProcessingFilter {

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        int w = area.width();
        int h = area.height();
        int centerX = w / 2;
        int centerY = h / 2;

        double time = context.runtime().milliseconds() / 500.0;
        int iterations = 30;

        for (int i = 0; i < iterations; i++) {
            double wave = Math.sin(time + (i * 0.3));
            int offset = (int) (wave * 12);

            int stepW = centerX / iterations;
            int stepH = centerY / iterations;

            // Lokale Koordinaten innerhalb der Area (0 bis w/h)
            int localSx1 = i * stepW;
            int localSy1 = i * stepH;
            int localSx2 = w - localSx1;
            int localSy2 = h - localSy1;

            // Ziel-Koordinaten inklusive Wellen-Offset
            int localDx1 = localSx1 - offset;
            int localDy1 = localSy1 - offset;
            int localDx2 = localSx2 + offset;
            int localDy2 = localSy2 + offset;

            // Zeichnen unter Berücksichtigung der absoluten Area-Position
            target.drawImage(source,
                area.x() + localDx1, area.y() + localDy1, area.x() + localDx2, area.y() + localDy2,
                area.x() + localSx1, area.y() + localSy1, area.x() + localSx2, area.y() + localSy2,
                null);
        }
    }
}
