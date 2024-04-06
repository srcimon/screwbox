package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public enum SpritesBundle implements AssetBundle<Sprite> {

    MOON_SURFACE_16(Asset.asset(() -> Sprite.fromFile("assets/sprites/MOON_SURFACE_16.png"))),
    BLOB_ANIMATED_16(Asset.asset(() -> Sprite.animatedFromFile("assets/sprites/BLOB_ANIMATED_16.png", Size.square(16), Duration.ofMillis(150))));

    private final Asset<Sprite> asset;

    SpritesBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
