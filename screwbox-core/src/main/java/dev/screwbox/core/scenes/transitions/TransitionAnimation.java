package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

@FunctionalInterface
public interface TransitionAnimation {

    void apply(Image source, Graphics2D target, PostProcessingContext context, Percent progress);

}
