package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
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
    public void renderSelectableItem(String label, ScreenBounds bounds, Screen screen) {
        if (!opacity.isZero()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, WHITE.opacity(opacity));
        }
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Screen screen) {
        if (!opacity.isZero()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, RED.opacity(opacity));
        }
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Screen screen) {
        if (!opacity.isZero()) {
            screen.drawText(at(bounds.offset().x(), bounds.center().y()), label, FONT, GREY.opacity(opacity));
        }
    }

}
