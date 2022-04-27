package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.window.Window;

public interface UiRenderer {

    void render(UiMenu menu, UiLayouter layouter, Window window);
}
