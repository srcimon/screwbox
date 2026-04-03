package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Creates a heat haze effect.
 *
 * @since 3.24.0
 */
public record HeatHazePostFilter(Duration interval, int segmentHeight) implements PostProcessingFilter {

    public HeatHazePostFilter {
        Validate.range(segmentHeight, 2, 48, "segment height must be in range 2 to 48");
    }

    public HeatHazePostFilter() {
        this(Duration.ofMillis(500), 2);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        final int scaledSegmentHeight = (int) Math.max(1, segmentHeight * scale);

        for (int y = area.y(); y < area.maxY(); y += scaledSegmentHeight) {
            final double verticalFactor = (double) (y - area.y()) / area.height();

            final int offsetX = (int) (((Math.sin(time * 1.5 + y * (0.1 / scale)) * 4 +
                                         Math.sin(time * 3.7 + y * (0.5 / scale)) * 2) * verticalFactor) * scale);

            final int offsetY = (int) ((Math.abs(Math.sin(time * 0.5 + y * (0.05 / scale))) * 3 * verticalFactor) * scale);

            target.drawImage(source,
                area.x(), y, area.maxX(), y + scaledSegmentHeight,
                area.x() + offsetX, y + offsetY, area.maxX() + offsetX, y + scaledSegmentHeight + offsetY,
                null);
        }
    }
}