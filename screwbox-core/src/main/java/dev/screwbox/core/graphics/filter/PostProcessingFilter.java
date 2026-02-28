package dev.screwbox.core.graphics.filter;

import dev.screwbox.core.graphics.ScreenBounds;

import java.awt.*;
import java.awt.image.VolatileImage;

public interface PostProcessingFilter {

    void apply(VolatileImage source, Graphics2D target, ScreenBounds filterArea, PostProcessingContext context);
}
