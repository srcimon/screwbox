package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;
import dev.screwbox.core.graphics.Viewport;

/**
 * Context that is provided when applieng {@link PostProcessingFilter}.
 *
 * @param backgroundColor configured color of the {@link Screen} background
 * @param lifetime        duration the {@link PostProcessingFilter} is already active
 * @param viewport        {@link Viewport} the {@link PostProcessingFilter} ist applied on
 * @since 3.24.0
 */
public record PostProcessingContext(
    Color backgroundColor,
    Duration lifetime,
    Viewport viewport) implements Sizeable {

    /**
     * Returns the canvas bounds of the {@link Viewport}.
     */
    public ScreenBounds bounds() {
        return viewport.canvas().bounds();
    }

    /**
     * Returns the {@link Size} of the {@link Viewport}.
     */
    @Override
    public Size size() {
        return bounds().size();
    }

}
