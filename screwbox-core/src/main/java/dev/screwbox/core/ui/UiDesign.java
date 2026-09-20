package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.Canvas;

/**
 * Specifies the design of ui menues.
 *
 * @see Ui#setDesign(UiDesign)
 */
public interface UiDesign {

    /**
     * Renders a selectable menu item.
     */
    void renderSelectableItem(String label, Canvas canvas);

    /**
     * Renders a selected menu item.
     */
    void renderSelectedItem(String label, Canvas canvas);

    /**
     * Renders an inactive menu item.
     */
    void renderInactiveItem(String label, Canvas canvas);
}
