package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.graphics.WindowBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, WindowBounds bounds, Screen screen);

    void renderSelectedItem(String label, WindowBounds bounds, Screen screen);

    void renderInactiveItem(String label, WindowBounds bounds, Screen screen);
}
