package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Viewport;

public class DefaultViewport implements Viewport {

    private final Renderer renderer;

    public DefaultViewport(final Renderer renderer) {
        this.renderer = renderer;
    }
}
