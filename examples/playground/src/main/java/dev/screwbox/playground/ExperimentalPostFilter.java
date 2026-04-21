package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExperimentalPostFilter implements PostProcessingFilter {

    private final Polygon outline;
    private final Polygon surface;
    Percent strength = Percent.of(0.3);
    private static final int ITERATIONS = 30;
    Duration interval = Duration.ofMillis(500);
    public ExperimentalPostFilter(Polygon outline, Polygon surface) {
        this.outline = outline;
        this.surface = surface;
    }

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final var originalClip = target.getClip();


        List<Offset> offsets = new ArrayList<>();
        for(var node : outline.definitionNotes()) {
            offsets.add(context.viewport().toCanvas(node));
        }
        var outline = AwtMapper.toPath(offsets);
        // Polygon-Setup (ohne die letzten beiden Knoten)

        final int stepX = (int) Math.ceil(area.center().x() / (double) ITERATIONS);
        final int stepY = (int) Math.ceil(area.center().y() / (double) ITERATIONS);
        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        // Effekt auf Polygon beschränken
        target.setClip(outline);

        for (int i = 0; i < ITERATIONS; i++) {
            final double wave = Math.sin(time + (i * strength.value()));
            final int offset = (int) (wave * 12 * scale);

            final int localSx1 = i * stepX;
            final int localSy1 = i * stepY;
            final int localSx2 = context.width() - localSx1;
            final int localSy2 = context.height() - localSy1;

            final int localDx1 = localSx1 - offset;
            final int localDy1 = localSy1 - offset;
            final int localDx2 = localSx2 + offset;
            final int localDy2 = localSy2 + offset;

            target.drawImage(source,
                area.x() + localDx1, area.y() + localDy1,
                area.x() + localDx2, area.y() + localDy2,
                area.x() + localSx1, area.y() + localSy1,
                area.x() + localSx2, area.y() + localSy2,
                null);
        }

        // Clip zurücksetzen für nachfolgende Effekte
        target.setClip(originalClip);
    }
}
