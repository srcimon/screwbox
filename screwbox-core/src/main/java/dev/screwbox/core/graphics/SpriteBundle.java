package dev.screwbox.core.graphics;

import dev.screwbox.core.Duration;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.graphics.Size.square;
import static dev.screwbox.core.graphics.Sprite.animatedAssetFromFile;
import static dev.screwbox.core.graphics.Sprite.assetFromFile;

/**
 * An {@link AssetBundle} for {@link Sprite}s shipped with the {@link ScrewBox} game engine.
 */
public enum SpriteBundle implements AssetBundle<Sprite> {

    ICON(assetFromFile("assets/sprites/ICON.png")),
    ICON_LARGE(assetFromFile("assets/sprites/ICON_LARGE.png")),
    ACHIEVEMENT(assetFromFile("assets/sprites/ACHIEVEMENT.png")),
    BOX_STRIPED(assetFromFile("assets/sprites/BOX_STRIPED.png")),
    DOT_BLUE(assetFromFile("assets/sprites/DOT_BLUE.png")),
    DOT_YELLOW(assetFromFile("assets/sprites/DOT_YELLOW.png")),
    DOT_RED(assetFromFile("assets/sprites/DOT_RED.png")),
    CLOUDS(assetFromFile("assets/sprites/CLOUDS.png")),
    SMOKE(assetFromFile("assets/sprites/SMOKE.png")),
    SLIME_MOVING(animatedAssetFromFile("assets/sprites/SLIME_MOVING.png", square(16), ofMillis(150))),
    TNT_TICKING(animatedAssetFromFile("assets/sprites/TNT_TICKING.png", square(16), ofMillis(150))),
    ELECTRICITY_SPARCLE(animatedAssetFromFile("assets/sprites/ELECTRICITY_SPARCLE.png", square(20), ofMillis(150))),
    LEAVE_FALLING(animatedAssetFromFile("assets/sprites/LEAVE_FALLING.png", Size.square(8), Duration.ofMillis(250))),
    EXPLOSION(animatedAssetFromFile("assets/sprites/EXPLOSION.png", square(16), ofMillis(150))),
    FIRE(animatedAssetFromFile("assets/sprites/FIRE.png", square(16), ofMillis(150))),
    MAN_STAND(animatedAssetFromFile("assets/sprites/MAN_STAND.png", square(16), ofMillis(150))),
    MAN_DISSOLVE(animatedAssetFromFile("assets/sprites/MAN_DISSOLVE.png", square(16), ofMillis(100))),
    MAN_WALK_BACK(animatedAssetFromFile("assets/sprites/MAN_WALK_BACK.png", square(16), ofMillis(150))),
    MAN_WALK_FRONT(animatedAssetFromFile("assets/sprites/MAN_WALK_FRONT.png", square(16), ofMillis(150))),
    MAN_WALK_LEFT(animatedAssetFromFile("assets/sprites/MAN_WALK_LEFT.png", square(16), ofMillis(150))),
    MAN_WALK_RIGHT(animatedAssetFromFile("assets/sprites/MAN_WALK_RIGHT.png", square(16), ofMillis(150))),
    MARKER_TARGET(animatedAssetFromFile("assets/sprites/MARKER_TARGET.png", square(16), ofMillis(150))),
    MARKER_SHIELD(animatedAssetFromFile("assets/sprites/MARKER_SHIELD.png", square(16), ofMillis(150))),
    MARKER_SKULL(animatedAssetFromFile("assets/sprites/MARKER_SKULL.png", square(16), ofMillis(150))),
    MARKER_CROSSHAIR(animatedAssetFromFile("assets/sprites/MARKER_CROSSHAIR.png", square(16), ofMillis(150))),
    MARKER_TNT(animatedAssetFromFile("assets/sprites/MARKER_TNT.png", square(16), ofMillis(150))),
    MARKER_SPECIAL(animatedAssetFromFile("assets/sprites/MARKER_SPECIAL.png", square(16), ofMillis(150))),
    MONSTER_FLYING(animatedAssetFromFile("assets/sprites/MONSTER_FLYING.png", square(32), ofMillis(150))),
    CRT_MONITOR_EDGE(assetFromFile("assets/sprites/CRT_MONITOR_EDGE.png")),
    SHADER_PREVIEW(assetFromFile("assets/sprites/SHADER_PREVIEW.png"));

    private final Asset<Sprite> asset;

    SpriteBundle(final Asset<Sprite> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<Sprite> asset() {
        return asset;
    }
}
