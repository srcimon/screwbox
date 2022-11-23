package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class WobblyUiLayouter implements UiLayouter {

    private final Time start = Time.now();

    @Override
    public WindowBounds calculateBounds(UiMenuItem item, UiMenu menu, Screen screen) {
        int itemIndex = menu.itemIndex(item);
        double waveSeed = Math.sin(Duration.since(start).milliseconds() / 600.0 + itemIndex);
        int x = (int) (waveSeed * 30);
        int heightOfItem = 50;
        int heightOfMenu = menu.itemCount() * heightOfItem;
        int y = screen.center().y() - heightOfMenu / 2 + (int) (waveSeed * 10);
        var offset = Offset.at(x, y + itemIndex * heightOfItem);
        var dimension = Dimension.of(screen.size().width(), heightOfItem);
        return new WindowBounds(offset, dimension);
    }

}
