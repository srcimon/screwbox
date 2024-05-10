package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.graphics.Size.square;
import static io.github.srcimon.screwbox.core.graphics.Sprite.animatedAssetFromFile;
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
    SLIME_WALKING(animatedAssetFromFile("assets/sprites/SLIME_WALKING.png", square(16), ofMillis(150))),
    TNT_TICKING(animatedAssetFromFile("assets/sprites/TNT_TICKING.png", square(16), ofMillis(150))),
    ELECTRICITY_SPARCLE(animatedAssetFromFile("assets/sprites/ELECTRICITY_SPARCLE.png", square(20), ofMillis(150))),
    MARKER_TARGET(animatedAssetFromFile("assets/sprites/MARKER_TARGET.png", square(16), ofMillis(150))),
    MARKER_SHIELD(animatedAssetFromFile("assets/sprites/MARKER_SHIELD.png", square(16), ofMillis(150))),
    MARKER_SKULL(animatedAssetFromFile("assets/sprites/MARKER_SKULL.png", square(16), ofMillis(150))),
    MARKER_TNT(animatedAssetFromFile("assets/sprites/MARKER_TNT.png", square(16), ofMillis(150))),
    MARKER_SPECIAL(animatedAssetFromFile("assets/sprites/MARKER_SPECIAL.png", square(16), ofMillis(150))),
    MONSTER_FLYING(animatedAssetFromFile("assets/sprites/MONSTER_FLYING.png", square(32), ofMillis(150)));

    private final Asset<Sprite> asset;

    SpriteBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
