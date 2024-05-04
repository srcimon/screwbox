package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.Color;

public enum ExtroAnimationBundle implements AssetBundle<ExtroAnimation> {

    FADE_TO_BLACK((screen, value) -> screen.fillWith(Color.BLACK.opacity(value)));

    private final ExtroAnimation animation;

    ExtroAnimationBundle(final ExtroAnimation animation) {
        this.animation = animation;
    }

    @Override
    public Asset<ExtroAnimation> asset() {
        return Asset.asset(() -> animation);
    }
}
