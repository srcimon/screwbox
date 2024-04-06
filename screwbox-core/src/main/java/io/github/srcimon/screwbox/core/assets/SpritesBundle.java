package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public enum SpritesBundle implements AssetBundle<Sprite> {

    MOON_SURFACE_16(Asset.asset(() -> Sprite.fromFile("assets/sprites/dummy_16x16.png"))),//TODO: RENAME FILE
    BLOB_MOVING_16(Asset.asset(() -> Sprite.animatedFromFile("assets/sprites/dummy_16x16_animated.png", Size.square(16), Duration.ofMillis(150))));//TODO: RENAME FILE

    private final Asset<Sprite> asset;

    SpritesBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
