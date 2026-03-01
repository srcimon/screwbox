package dev.screwbox.core.graphics.filter;

import java.awt.*;
import java.awt.image.VolatileImage;

@FunctionalInterface
public interface PostProcessingFilter {

    void apply(VolatileImage source, Graphics2D target, PostProcessingContext context);
}
