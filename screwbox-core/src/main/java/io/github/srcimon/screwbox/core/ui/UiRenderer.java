package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.RenderTarget;

public interface UiRenderer {

    void renderSelectableItem(String label, ScreenBounds bounds, RenderTarget rendertarget);

    void renderSelectedItem(String label, ScreenBounds bounds, RenderTarget rendertarget);

    void renderInactiveItem(String label, ScreenBounds bounds, RenderTarget rendertarget);
}
