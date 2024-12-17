package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.NotificationLayouter;

public class TopLeftNofiticationLayouter implements NotificationLayouter {

    @Override
    public ScreenBounds layout(final int index, final ScreenBounds canvasBounds) {
        return new ScreenBounds(4, index * 50, (int) (canvasBounds.width() / 3.0), 40);
    }
}
