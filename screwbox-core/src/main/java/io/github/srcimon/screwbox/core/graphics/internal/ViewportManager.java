package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;

public class ViewportManager {

    boolean isEnabled = false;

    public boolean isSplitScreenEnabled() {
        return isEnabled;
    }

    public void enableSplitScreen(SplitScreenOptions options) {
        isEnabled = true;
    }

    public void disableSplitScreen() {
        isEnabled = false;
    }
}
