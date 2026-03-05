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
        final int stepX = area.center().x() / ITERATIONS;
        final int stepY = area.center().y() / ITERATIONS;

        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        for (int i = 0; i < ITERATIONS; i++) {
            final double wave = Math.sin(time + (i * strength.value()));
            final int offset = (int) (wave * 12);

            final int localSx1 = i * stepX;
            final int localSy1 = i * stepY;
            final int localSx2 = context.width() - localSx1;
            final int localSy2 = context.height() - localSy1;

            final int localDx1 = localSx1 - offset;
            final int localDy1 = localSy1 - offset;
            final int localDx2 = localSx2 + offset;
            final int localDy2 = localSy2 + offset;

            target.drawImage(source,
                area.x() + localDx1, area.y() + localDy1, area.x() + localDx2, area.y() + localDy2,
                area.x() + localSx1, area.y() + localSy1, area.x() + localSx2, area.y() + localSy2,
                null);
        }
    }
}
