package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.ui.UiLayout;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiMenuItem;

public class WobblyUiLayout implements UiLayout {

    private final Time start = Time.now();

    @Override
    public ScreenBounds layout(UiMenuItem item, UiMenu menu, ScreenBounds bounds) {
        final int itemIndex = menu.itemIndex(item);
        final double waveSeed = Math.sin(Duration.since(start).milliseconds() / 600.0 + itemIndex);
        final int x = (int) (waveSeed * 30);
        final int heightOfItem = 50;
        final int heightOfMenu = menu.itemCount() * heightOfItem;
        final int y = bounds.center().y() - heightOfMenu / 2 + (int) (waveSeed * 10);
        final var offset = Offset.at(x, y + itemIndex * heightOfItem);
        final var dimension = Size.of(bounds.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
