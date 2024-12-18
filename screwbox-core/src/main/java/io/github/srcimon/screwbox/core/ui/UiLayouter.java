package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

public interface UiLayouter {

    ScreenBounds layout(UiMenuItem item, UiMenu menu, ScreenBounds renderArea);

}
