package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

@FunctionalInterface
public interface TransitionPostFilter {

    void apply(Image source, Graphics2D target, PostProcessingContext context, Percent progress);

}
