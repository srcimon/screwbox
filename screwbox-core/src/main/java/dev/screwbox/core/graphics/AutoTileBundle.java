package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;

public enum AutoTileBundle implements AssetBundle<AutoTile> {

    TEMPLATE_3X3(AutoTile.assetFromSpriteSheet("assets/autotiles/template3x3.png")),
    ROCKS(AutoTile.assetFromSpriteSheet("assets/autotiles/rocks.png")),
    TEMPLATE_2X2(AutoTile.assetFromSpriteSheet("assets/autotiles/template2x2.png")),
    GRASS(AutoTile.assetFromSpriteSheet("assets/autotiles/grass.png"));

    private final Asset<AutoTile> asset;

    AutoTileBundle(final Asset<AutoTile> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<AutoTile> asset() {
        return asset;
    }

}
