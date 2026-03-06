package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

//TODO Test and document
public record WavePostFilter(int rowHeight, Duration interval, double intensity, Percent frequency) implements PostProcessingFilter {

    public WavePostFilter {
        Validate.range(rowHeight, 1, 16, "row height must be in range 1 to 16");
        Validate.range(intensity, 1, 32, "intensity must be in range 1 to 32");
        Validate.range(intensity, 1, 32, "intensity must be in range 1 to 32");
    }

    public WavePostFilter() {
        this(2, Duration.ofMillis(500), 10, Percent.of(0.05));
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        target.setColor(AwtMapper.toAwtColor(context.backgroundColor()));
        target.fillRect(area.x(), area.y(), area.width(), area.height());

        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        for (int y = 0; y < area.height(); y += rowHeight) {
            int offsetX = (int) (Math.sin((y * frequency.value()) + time) * intensity);

            target.drawImage(source,
                area.x() + offsetX, area.y() + y, area.maxX() + offsetX, area.y() + y + rowHeight,
                area.x(), area.y() + y, area.maxX(), area.y() + y + rowHeight,
                null);
        }
    }
}
