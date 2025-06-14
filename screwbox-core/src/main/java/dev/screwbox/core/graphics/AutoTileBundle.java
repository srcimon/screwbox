package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;

public enum AutoTileBundle implements AssetBundle<AutoTile> {

    TEMPLATE_2X2(AutoTile.assetFromSpriteSheet("assets/autotiles/template_2x2.png", AutoTile.Template.TEMPLATE_2X2)),
    TEMPLATE_3X3(AutoTile.assetFromSpriteSheet("assets/autotiles/template_3x3.png", AutoTile.Template.TEMPLATE_3X3)),
    ROCKS(AutoTile.assetFromSpriteSheet("assets/autotiles/rocks.png", AutoTile.Template.TEMPLATE_3X3)),
    CANDYLAND(AutoTile.assetFromSpriteSheet("assets/autotiles/candyland.png", AutoTile.Template.TEMPLATE_2X2));

    private final Asset<AutoTile> asset;

    AutoTileBundle(final Asset<AutoTile> asset) {
        this.asset = asset;
    }

    @Override
    public Asset<AutoTile> asset() {
        return asset;
    }

}
