package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Viewport;

public record PostProcessingContext(Color backgroundColor, Time time, Duration runtime, Viewport viewport) {//TODO replace time and runtime with single indicator

    public ScreenBounds bounds() {
        return viewport.canvas().bounds();
    }
}
