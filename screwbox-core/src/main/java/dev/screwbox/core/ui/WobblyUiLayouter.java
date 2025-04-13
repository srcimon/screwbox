package dev.screwbox.core.ui;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;

public class WobblyUiLayouter implements UiLayouter {

    private final Time start = Time.now();

    @Override
    public ScreenBounds layout(UiMenuItem item, UiMenu menu, ScreenBounds renderArea) {
        final int itemIndex = menu.itemIndex(item);
        final double waveSeed = Math.sin(Duration.since(start).milliseconds() / 600.0 + itemIndex);
        final int x = (int) (waveSeed * 30);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = renderArea.center().y() - heightOfMenu / 2 + (int) (waveSeed * 10);
        final var offset = Offset.at(x, y + itemIndex * heightOfItem);
        final var dimension = Size.of(renderArea.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
