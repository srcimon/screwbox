package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.assets.Asset.asset;

public enum BundledSprites implements Supplier<Sprite> {

    MOON_SURFACE_16(asset(() -> Sprite.fromFile("assets/sprites/dummy_16x16.png"))),//TODO: RENAME FILE
    BLOB_MOVING_16(asset(() -> Sprite.animatedFromFile("assets/sprites/dummy_16x16_animated.png", Size.square(16), Duration.ofMillis(150))));//TODO: RENAME FILE

    private final Asset<Sprite> asset;

    BundledSprites(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Sprite get() {
        return asset.get().freshInstance();
    }
}
