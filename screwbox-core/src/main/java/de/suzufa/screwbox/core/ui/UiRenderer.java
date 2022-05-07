package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.graphics.Window;

public interface UiRenderer {

    void render(UiMenu menu, UiLayouter layouter, Window window);
}
