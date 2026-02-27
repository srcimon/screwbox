package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.PostProcessing;

import java.awt.image.VolatileImage;

public class DefaultPostProcessing implements PostProcessing {

    public VolatileImage applyPostprocessing(final VolatileImage screen) {
        return screen;
    }

    public boolean isActive() {
        return true;
    }
}
