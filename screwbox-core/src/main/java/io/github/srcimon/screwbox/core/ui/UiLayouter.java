package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

public interface UiLayouter {

    ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen);

}
