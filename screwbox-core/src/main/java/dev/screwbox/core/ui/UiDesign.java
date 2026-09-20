package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.Canvas;

public interface UiDesign {

    void renderSelectableItem(String label, Canvas canvas);

    void renderSelectedItem(String label, Canvas canvas);

    void renderInactiveItem(String label, Canvas canvas);
}
