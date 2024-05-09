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

    ICON(assetFromFile("assets/sprites/ICON.png")),
    ICON_LARGE(assetFromFile("assets/sprites/ICON_LARGE.png")),
    BOX_STRIPED(assetFromFile("assets/sprites/BOX_STRIPED.png")),
    DOT_BLUE(assetFromFile("assets/sprites/DOT_BLUE.png")),
    DOT_YELLOW(assetFromFile("assets/sprites/DOT_YELLOW.png")),
    DOT_RED(assetFromFile("assets/sprites/DOT_RED.png")),
    SMOKE(assetFromFile("assets/sprites/SMOKE.png")),
    SLIME_WALKING(Asset.asset(() -> Sprite.animatedFromFile("assets/sprites/SLIME_WALKING.png", Size.square(16), Duration.ofMillis(150))));

    private final Asset<Sprite> asset;

    SpriteBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
