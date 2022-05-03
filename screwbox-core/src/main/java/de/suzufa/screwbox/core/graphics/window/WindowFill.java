package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;

public record WindowFill(Color color) {

    public static WindowFill fill(Color color) {
        return new WindowFill(color);
    }
}
