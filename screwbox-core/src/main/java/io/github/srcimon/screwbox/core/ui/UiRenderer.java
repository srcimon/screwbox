package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, WindowBounds bounds, Screen screen);

    void renderSelectedItem(String label, WindowBounds bounds, Screen screen);

    void renderInactiveItem(String label, WindowBounds bounds, Screen screen);
}
