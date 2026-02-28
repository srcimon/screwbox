package dev.screwbox.core.graphics.effects;

import java.awt.*;
import java.awt.image.VolatileImage;

public interface PostProcessingFilter {

    void apply(final VolatileImage source, final Graphics2D target, PostProcessingContext context);
}
