package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.graphics.WindowBounds;

public interface UiLayouter {

    WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen);

}
