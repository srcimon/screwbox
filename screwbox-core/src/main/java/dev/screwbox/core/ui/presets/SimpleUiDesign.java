package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.ui.UiDesign;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.graphics.Color.GREY;
import static dev.screwbox.core.graphics.Color.WHITE;
import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

public class SimpleUiDesign implements UiDesign {

    private static final TextDrawOptions SELECTABLE = font(BOLDZILLA)
        .scale(2)
        .alignCenter()
        .styleFont(1 , BOLDZILLA.customColorAsset(GREY));

    private static final TextDrawOptions SELECTED = font(BOLDZILLA.customColorAsset(YELLOW))
        .scale(2.5)
        .alignCenter()
        .styleFont(1 , BOLDZILLA.customColorAsset(Color.hex("#fff77a")));

    private static final TextDrawOptions INACTIVE = font(BOLDZILLA.customColorAsset(WHITE.opacity(0.2)))
        .scale(2)
        .alignCenter()
        .styleFont(1 , BOLDZILLA.customColorAsset(GREY));

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, SELECTABLE);
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, SELECTED);
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Canvas canvas) {
        canvas.drawText(bounds.center(), label, INACTIVE);
    }

}
