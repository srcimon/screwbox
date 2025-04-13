package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.ScreenBounds;

public interface UiLayouter {

    ScreenBounds layout(UiMenuItem item, UiMenu menu, ScreenBounds renderArea);

}
