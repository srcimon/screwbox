package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface UiLayouter {

    WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Window window);

}
