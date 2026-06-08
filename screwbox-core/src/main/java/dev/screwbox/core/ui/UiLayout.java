package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.ScreenBounds;

@FunctionalInterface
public interface UiLayout {

    ScreenBounds layout(UiMenuItem item, UiMenu menu, ScreenBounds bounds);

}
