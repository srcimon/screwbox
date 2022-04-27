package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.window.Window;

public interface UiLayouter {

    WindowBounds screenBoundsOf(UiMenuItem item, UiMenu menu, Window window);

}
