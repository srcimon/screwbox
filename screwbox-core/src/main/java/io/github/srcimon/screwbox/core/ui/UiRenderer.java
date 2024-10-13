package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.Rendertarget;

public interface UiRenderer {

    void renderSelectableItem(String label, ScreenBounds bounds, Rendertarget rendertarget);

    void renderSelectedItem(String label, ScreenBounds bounds, Rendertarget rendertarget);

    void renderInactiveItem(String label, ScreenBounds bounds, Rendertarget rendertarget);
}
