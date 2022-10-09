package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface UiRenderer {

    void renderSelectableItem(String label, WindowBounds bounds, Window window);

    void renderSelectedItem(String label, WindowBounds bounds, Window window);

    void renderInactiveItem(String label, WindowBounds bounds, Window window);
}
