package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;
import dev.screwbox.core.graphics.Viewport;

public record PostProcessingContext(
    Color backgroundColor,
    Duration filterActiveDuration,
    Viewport viewport) implements Sizeable {

    public ScreenBounds bounds() {
        return viewport.canvas().bounds();
    }

    @Override
    public Size size() {
        return bounds().size();
    }

}
