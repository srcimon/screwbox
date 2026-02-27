package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.PostProcessing;

import java.awt.image.VolatileImage;

public class DefaultPostProcessing implements PostProcessing {

    public VolatileImage applyPostprocessing(VolatileImage screen) {
        return screen;
    }
}
