package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.NotificationLayouter;


public class TopLeftNofiticationLayouter implements NotificationLayouter {

    private static final int PADDING = 2;
    private static final int HEIGHT = 50;

    @Override
    public ScreenBounds layout(final int index, final ScreenBounds canvasBounds) {
        return new ScreenBounds(PADDING, PADDING + index * (PADDING + HEIGHT), (int) (canvasBounds.width() / 3.0 - 2 * PADDING), HEIGHT);
    }
}
