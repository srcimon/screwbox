package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.Rendertarget;

public interface UiLayouter {

    ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, Size size);

}
