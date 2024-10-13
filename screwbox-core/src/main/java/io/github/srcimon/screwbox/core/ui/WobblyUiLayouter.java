package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.Rendertarget;

public class WobblyUiLayouter implements UiLayouter {

    private final Time start = Time.now();

    @Override
    public ScreenBounds calculateBounds(UiMenuItem item, UiMenu menu, Size size) {
        int itemIndex = menu.itemIndex(item);
        double waveSeed = Math.sin(Duration.since(start).milliseconds() / 600.0 + itemIndex);
        int x = (int) (waveSeed * 30);
        int heightOfItem = 50;
        int heightOfMenu = menu.itemCount() * heightOfItem;
        int y = size.center().y() - heightOfMenu / 2 + (int) (waveSeed * 10);
        var offset = Offset.at(x, y + itemIndex * heightOfItem);
        var dimension = Size.of(size.width(), heightOfItem);
        return new ScreenBounds(offset, dimension);
    }

}
