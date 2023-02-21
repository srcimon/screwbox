package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Pixelfont;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.graphics.WindowBounds;

public class SimpleUiRenderer implements UiRenderer {

    private static final Pixelfont NOT_SELECTED = Pixelfont.defaultFont(Color.WHITE);
    private static final Pixelfont SELECTED = Pixelfont.defaultFont(Color.YELLOW);
    private static final Pixelfont INACTIVE = Pixelfont.defaultFont(Color.WHITE.opacity(0.2));

    @Override
    public void renderSelectableItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, NOT_SELECTED, 2);
    }

    @Override
    public void renderSelectedItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, SELECTED, 2.5);
    }

    @Override
    public void renderInactiveItem(String label, WindowBounds bounds, Screen screen) {
        screen.drawTextCentered(bounds.center(), label, INACTIVE, 2);
    }

}
