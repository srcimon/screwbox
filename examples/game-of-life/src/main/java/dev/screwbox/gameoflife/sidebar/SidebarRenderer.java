package dev.screwbox.gameoflife.sidebar;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.ui.UiRenderer;

import static dev.screwbox.core.graphics.Color.GREY;
import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.WHITE;
import static dev.screwbox.core.graphics.Offset.at;

public class SidebarRenderer implements UiRenderer {

    private final Percent opacity;

    public SidebarRenderer(final Percent opacity) {
        this.opacity = opacity;
    }

    private static final SystemTextDrawOptions OPTIONS = SystemTextDrawOptions.systemFont("Arial", 18).bold();

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(bounds.offset().x(), bounds.center().y()), label, OPTIONS.color(WHITE.opacity(opacity)));
        }
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(bounds.offset().x(), bounds.center().y()), label, OPTIONS.color(RED.opacity(opacity)));
        }
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(bounds.offset().x(), bounds.center().y()), label, OPTIONS.color(GREY.opacity(opacity)));
        }
    }

}
