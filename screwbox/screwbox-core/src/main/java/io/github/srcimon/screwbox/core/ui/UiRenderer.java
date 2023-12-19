package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, ScreenBounds bounds, Screen screen);

    void renderSelectedItem(String label, ScreenBounds bounds, Screen screen);

    void renderInactiveItem(String label, ScreenBounds bounds, Screen screen);
}
