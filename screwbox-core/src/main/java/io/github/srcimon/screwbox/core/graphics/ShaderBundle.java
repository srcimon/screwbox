package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;
import io.github.srcimon.screwbox.core.graphics.shader.WaterDistortionShader;

//TODO add to bundle documentation
//TODO add test for AssetBundles in bundle documentation
public enum ShaderBundle implements AssetBundle<ShaderOptions> {

    WATER(ShaderOptions.shader(new WaterDistortionShader()).cacheSize(30)),
    BREEZE(ShaderOptions.shader(new WaterDistortionShader(2, 0.25)).duration(Duration.ofSeconds(4)).cacheSize(30));

    private final Asset<ShaderOptions> options;

    ShaderBundle(final ShaderOptions options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderOptions> asset() {
        return options;
    }
}
