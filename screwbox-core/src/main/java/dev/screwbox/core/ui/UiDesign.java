package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.Canvas;

/**
 * Specifies the design of ui menues.
 *
 * @see Ui#setDesign(UiDesign)
 */
public interface UiDesign {
//TODO finish javadoc
    void renderSelectableItem(String label, Canvas canvas);

    void renderSelectedItem(String label, Canvas canvas);

    void renderInactiveItem(String label, Canvas canvas);
}
