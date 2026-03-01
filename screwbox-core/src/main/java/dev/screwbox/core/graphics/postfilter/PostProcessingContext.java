package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Viewport;

public record PostProcessingContext(Color backgroundColor, Duration filterActiveDuration, Viewport viewport) {

    public ScreenBounds bounds() {
        return viewport.canvas().bounds();
    }

    public int width() {
        return bounds().width();
    }

    public int height() {
        return bounds().height();
    }
}
