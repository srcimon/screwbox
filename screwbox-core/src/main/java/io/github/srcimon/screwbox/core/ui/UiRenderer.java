package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Canvas;

public interface UiRenderer {

    void renderSelectableItem(String label, Canvas canvas);

    void renderSelectedItem(String label, Canvas canvas);

    void renderInactiveItem(String label, Canvas canvas);
}
