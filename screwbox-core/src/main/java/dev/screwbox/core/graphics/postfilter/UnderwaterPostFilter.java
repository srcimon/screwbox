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

    private static final int ITERATIONS = 30;

    public UnderwaterPostFilter() {
        this(Duration.ofMillis(500), Percent.of(0.3));
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        int stepW = area.center().x() / ITERATIONS;
        int stepH = area.center().y() / ITERATIONS;

        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        for (int i = 0; i < ITERATIONS; i++) {
            double wave = Math.sin(time + (i * strength.value()));
            int offset = (int) (wave * 12);

            int localSx1 = i * stepW;
            int localSy1 = i * stepH;
            int localSx2 = area.width() - localSx1;
            int localSy2 = area.height() - localSy1;

            int localDx1 = localSx1 - offset;
            int localDy1 = localSy1 - offset;
            int localDx2 = localSx2 + offset;
            int localDy2 = localSy2 + offset;

            target.drawImage(source,
                area.x() + localDx1, area.y() + localDy1, area.x() + localDx2, area.y() + localDy2,
                area.x() + localSx1, area.y() + localSy1, area.x() + localSx2, area.y() + localSy2,
                null);
        }
    }
}
