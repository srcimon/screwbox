package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Viewport;

public record PostProcessingContext(Color backgroundColor, Duration runtime, Viewport viewport) {//TODO replace time and runtime with single indicator to get rid of loop dependency

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
