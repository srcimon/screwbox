package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;

import static io.github.srcimon.screwbox.core.graphics.Sprite.assetFromFile;

/**
 * An {@link AssetBundle} for {@link Sprite}s shipped with the {@link ScrewBox} game engine.
 */
public enum SpriteBundle implements AssetBundle<Sprite> {

    ICON_32(assetFromFile("assets/sprites/ICON_32.png")),
    ICON_512(assetFromFile("assets/sprites/ICON_512.png")),
    BOX_STRIPED_32(assetFromFile("assets/sprites/BOX_STRIPED_32.png")),
    DOT_BLUE_16(assetFromFile("assets/sprites/DOT_BLUE_16.png")),
    DOT_YELLOW_16(assetFromFile("assets/sprites/DOT_YELLOW_16.png")),
    DOT_RED_16(assetFromFile("assets/sprites/DOT_RED_16.png")),
    MOON_SURFACE_16(assetFromFile("assets/sprites/MOON_SURFACE_16.png")),
    SMOKE_16(assetFromFile("assets/sprites/SMOKE_16.png")),
    BLOB_ANIMATED_16(Asset.asset(() -> Sprite.animatedFromFile("assets/sprites/BLOB_ANIMATED_16.png", Size.square(16), Duration.ofMillis(150))));

    private final Asset<Sprite> asset;

    SpriteBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
