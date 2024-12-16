package io.github.srcimon.screwbox.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.ui.UiRenderer;

import static io.github.srcimon.screwbox.core.graphics.Color.GREY;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Offset.at;

public class SidebarRenderer implements UiRenderer {

    private final Percent opacity;

    public SidebarRenderer(final Percent opacity) {
        this.opacity = opacity;
    }

    private static final SystemTextDrawOptions OPTIONS = SystemTextDrawOptions.systemFont("Arial", 18).bold();

    @Override
    public void renderSelectableItem(String label, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(canvas.offset().x(), canvas.center().y()), label, OPTIONS.color(WHITE.opacity(opacity)));
        }
    }

    @Override
    public void renderSelectedItem(String label, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(canvas.offset().x(), canvas.center().y()), label, OPTIONS.color(RED.opacity(opacity)));
        }
    }

    @Override
    public void renderInactiveItem(String label, Canvas canvas) {
        if (!opacity.isZero()) {
            canvas.drawText(at(canvas.offset().x(), canvas.center().y()), label, OPTIONS.color(GREY.opacity(opacity)));
        }
    }

}
