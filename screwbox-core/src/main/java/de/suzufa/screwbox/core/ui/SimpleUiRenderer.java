package de.suzufa.screwbox.core.ui;

import static de.suzufa.screwbox.core.graphics.Color.WHITE;
import static de.suzufa.screwbox.core.graphics.Color.YELLOW;

import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;

public class SimpleUiRenderer implements UiRenderer {

    private static final Font FONT_NOT_SELECTED = new Font("Arial", 20, Style.BOLD);
    private static final Font FONT_SELECTED = FONT_NOT_SELECTED.withSize(26);

    @Override
    public void render(final UiMenu menu, final UiLayouter layouter, final Window window) {
        for (final var item : menu.items()) {
            final boolean isActive = menu.isActiveItem(item);
            final var color = isActive ? YELLOW : WHITE;

            final var screenBounds = layouter.screenBoundsOf(item, menu, window);
            final Offset offset = screenBounds.center();
            if (window.isVisible(screenBounds)) {
                Font font = isActive ? FONT_SELECTED : FONT_NOT_SELECTED;
                window.drawTextCentered(offset, item.label(), font, color);
            }
        }
    }

}
