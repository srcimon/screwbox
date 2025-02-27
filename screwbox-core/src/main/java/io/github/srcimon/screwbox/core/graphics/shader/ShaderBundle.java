package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;

//TODO add to bundle documentation
//TODO add test for AssetBundles in bundle documentation
public enum ShaderBundle implements AssetBundle<ShaderOptions> {

    WATER(ShaderOptions.shader(new WaterDistortionShader()).cacheSize(30));

    private final Asset<ShaderOptions> options;

    ShaderBundle(final ShaderOptions options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderOptions> asset() {
        return options;
    }
}
