package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.NotificationLayouter;


public class TopLeftNofiticationLayouter implements NotificationLayouter {

    private final int height;
    private final int padding;

    public TopLeftNofiticationLayouter() {
        this(32, 2);
    }

    public TopLeftNofiticationLayouter(int height, int padding) {
        this.height = height;
        this.padding = padding;
    }

    @Override
    public ScreenBounds layout(final int index, final ScreenBounds canvasBounds) {
        return new ScreenBounds(padding, padding + index * (padding + height), canvasBounds.width() - 2 * padding, height);
    }
}
