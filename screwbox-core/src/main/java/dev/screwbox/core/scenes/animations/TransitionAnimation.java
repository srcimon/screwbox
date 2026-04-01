package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;

/**
 * An animation played when leaving or entering a {@link dev.screwbox.core.scenes.Scene}. Works similar to
 * {@link PostProcessingFilter} but also uses progress value to specify animation position.
 *
 * @since 3.26.0
 */
@FunctionalInterface
public interface TransitionAnimation {

    //TODO document
    void apply(Image source, Graphics2D target, Size size, Percent progress);
}
