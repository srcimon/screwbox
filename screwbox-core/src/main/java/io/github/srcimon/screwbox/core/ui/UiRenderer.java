package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, ScreenBounds bounds, Canvas canvas);

    void renderSelectedItem(String label, ScreenBounds bounds, Canvas canvas);

    void renderInactiveItem(String label, ScreenBounds bounds, Canvas canvas);
}
