package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;
import io.github.srcimon.screwbox.core.ui.UiRenderer;

import static io.github.srcimon.screwbox.core.graphics.Color.*;
import static io.github.srcimon.screwbox.core.graphics.Offset.at;

public class SidebarRenderer implements UiRenderer {

    private final Percent opacity;

    public SidebarRenderer(final Percent opacity) {
        this.opacity = opacity;
    }

    private static final Font FONT = new Font("Arial", 18, Font.Style.BOLD);

    @Override
    public void renderSelectableItem(String label, WindowBounds bounds, Screen screen) {
        if (!opacity.isMinValue()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, WHITE.opacity(opacity));
        }
    }

    @Override
    public void renderSelectedItem(String label, WindowBounds bounds, Screen screen) {
        if (!opacity.isMinValue()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, RED.opacity(opacity));
        }
    }

    @Override
    public void renderInactiveItem(String label, WindowBounds bounds, Screen screen) {
        if (!opacity.isMinValue()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, GREY.opacity(opacity));
        }
    }

}
