package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.ui.UiRenderer;

import static io.github.srcimon.screwbox.core.graphics.Offset.at;

public class SidebarRenderer implements UiRenderer {

    private static final Font FONT = new Font("Arial", 18, Font.Style.BOLD);

    @Override
    public void renderSelectableItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, Color.WHITE);
    }

    @Override
    public void renderSelectedItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, Color.RED);
    }

    @Override
    public void renderInactiveItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawText(at(bounds.offset().x(), bounds.center().y()),label, FONT, Color.WHITE.opacity(0.5));
    }

}
