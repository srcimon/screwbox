package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.ScreenBounds;

public interface UiDesign {

    void renderSelectableItem(String label, ScreenBounds bounds, Canvas canvas);

    void renderSelectedItem(String label, ScreenBounds bounds, Canvas canvas);

    void renderInactiveItem(String label, ScreenBounds bounds, Canvas canvas);
}
