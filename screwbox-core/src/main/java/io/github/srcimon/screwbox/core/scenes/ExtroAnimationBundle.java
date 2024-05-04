package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;

import java.util.function.Supplier;

public enum ExtroAnimationBundle implements AssetBundle<ExtroAnimation> {

    FADE_TO_BLACK(() -> (screen, value) -> screen.fillWith(Color.BLACK.opacity(value))),
    CIRCLES(() -> (screen, value) -> {
        int size = Math.max(screen.size().width(), screen.size().height()) / 20;
        int xDelta = screen.size().width() / (screen.size().width() / size);
        int yDelta = screen.size().height() / (screen.size().height() / size);

        for(int x = 0; x < screen.size().width() + xDelta; x += xDelta) {
            for (int y = 0; y < screen.size().height() +yDelta; y += yDelta) {
                screen.drawCircle(Offset.at(x, y), (int) (value.value() * size), CircleDrawOptions.filled(Color.BLACK));
            }
        }
    });

    private final Asset<ExtroAnimation> animation;

    ExtroAnimationBundle(final Supplier<ExtroAnimation> animation) {
        this.animation = Asset.asset(animation);
    }

    @Override
    public Asset<ExtroAnimation> asset() {
        return animation;
    }
}
