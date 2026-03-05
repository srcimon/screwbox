package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;

import java.awt.*;

/**
 * Distorts the image as if underwater.
 *
 * @since 3.24.0
 */
public record UnderwaterPostFilter(Duration interval, Percent strength) implements PostProcessingFilter {

    //TODO Support viewports
    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final var center = area.center();

        double time = context.lifetime().milliseconds() / (double) interval.milliseconds();
        int iterations = 30;

        for (int i = 0; i < iterations; i++) {
            double wave = Math.sin(time + (i * strength.value()));
            int offset = (int) (wave * 12);


            int stepW = center.x() / iterations;
            int stepH = center.y() / iterations;

            // Lokale Koordinaten innerhalb der Area (0 bis w/h)
            int localSx1 = i * stepW;
            int localSy1 = i * stepH;
            int localSx2 = area.width() - localSx1;
            int localSy2 = area.height() - localSy1;

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
