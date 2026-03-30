package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

@FunctionalInterface
public interface TransitionAnimation {

    void apply(Image source, Graphics2D target, PostProcessingContext context, Percent progress);

    //TODO allow overdrawing last scene for all transitions
    default void drawSourceImage(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();

        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);
    }
}
