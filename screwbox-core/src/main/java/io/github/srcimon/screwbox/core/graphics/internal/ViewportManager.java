package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;

public class ViewportManager {

    private final Viewport defaultViewport;
    private boolean isEnabled = false;

    public ViewportManager(final Viewport defaultViewport) {
        this.defaultViewport = defaultViewport;
    }
    public boolean isSplitScreenEnabled() {
        return isEnabled;
    }

    public void enableSplitScreen(final SplitScreenOptions options) {
        isEnabled = true;
    }

    public void disableSplitScreen() {
        isEnabled = false;
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }
}
