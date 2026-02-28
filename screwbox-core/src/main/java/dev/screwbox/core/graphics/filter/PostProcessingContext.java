package dev.screwbox.core.graphics.filter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Viewport;

public record PostProcessingContext(Color backgroundColor, Time time, Duration runtime, Vector cameraPosition, double zoom, Viewport viewport) {

    public ScreenBounds bounds() {
        return viewport.canvas().bounds();
    }
}
