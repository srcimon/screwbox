package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface UiRenderer {

    void renderSelectableItem(UiMenuItem item, WindowBounds bounds, Window window);

    void renderSelectedItem(UiMenuItem item, WindowBounds bounds, Window window);
}
