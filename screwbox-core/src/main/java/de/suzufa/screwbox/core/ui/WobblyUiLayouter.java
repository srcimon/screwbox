package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class WobblyUiLayouter implements UiLayouter {

    private final Time start = Time.now();

    @Override
    public WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Window window) {
        int itemIndex = menu.itemIndex(item);
        int x = (int) (Math.sin(Duration.since(start).milliseconds() / 600.0 + itemIndex) * 30);
        int heightOfItem = 50;
        int heightOfMenu = menu.itemCount() * heightOfItem;
        int y = window.center().y() - heightOfMenu / 2;

        var offset = Offset.at(x, y + itemIndex * heightOfItem);
        var dimension = Dimension.of(window.size().width(), heightOfItem);
        return new WindowBounds(offset, dimension);
    }

}
