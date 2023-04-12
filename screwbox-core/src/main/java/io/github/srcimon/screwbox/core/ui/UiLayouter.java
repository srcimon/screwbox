package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;

public interface UiLayouter {

    WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen);

}
