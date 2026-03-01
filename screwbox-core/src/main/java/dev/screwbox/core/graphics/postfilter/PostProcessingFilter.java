package dev.screwbox.core.graphics.postfilter;

import java.awt.*;

@FunctionalInterface
public interface PostProcessingFilter {

    void apply(Image source, Graphics2D target, PostProcessingContext context);
}
