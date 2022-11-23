package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, WindowBounds bounds, Screen screen);

    void renderSelectedItem(String label, WindowBounds bounds, Screen screen);

    void renderInactiveItem(String label, WindowBounds bounds, Screen screen);
}
