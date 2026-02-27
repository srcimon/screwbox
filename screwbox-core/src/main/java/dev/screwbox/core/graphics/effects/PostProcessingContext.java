package dev.screwbox.core.graphics.effects;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

public record PostProcessingContext(Color backgroundColor, Time time, Duration runtime, Vector cameraPosition, double zoom) {
}
