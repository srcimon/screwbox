package dev.screwbox.core.graphics.effects;

import java.awt.*;
import java.awt.image.VolatileImage;

public interface PostProcessingEffect {

    void apply(final VolatileImage source, final Graphics2D target);//TODO third var: PostProcessingContext (camera pos?)
}
