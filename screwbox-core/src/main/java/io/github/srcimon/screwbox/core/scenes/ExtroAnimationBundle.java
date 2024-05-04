package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.Color;

import java.util.function.Supplier;

public enum ExtroAnimationBundle implements AssetBundle<ExtroAnimation> {

    FADE_TO_BLACK(() -> (screen, value) -> screen.fillWith(Color.BLACK.opacity(value)));

    private final Asset<ExtroAnimation> animation;

    ExtroAnimationBundle(final Supplier<ExtroAnimation> animation) {
        this.animation = Asset.asset(animation);
    }

    @Override
    public Asset<ExtroAnimation> asset() {
        return animation;
    }
}
