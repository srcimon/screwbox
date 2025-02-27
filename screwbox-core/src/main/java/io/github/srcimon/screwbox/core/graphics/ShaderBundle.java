package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.graphics.shader.GrayscaleShader;
import io.github.srcimon.screwbox.core.graphics.shader.WaterDistortionShader;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions.shader;

//TODO add to bundle documentation
//TODO add test for AssetBundles in bundle documentation
public enum ShaderBundle implements AssetBundle<ShaderOptions> {

    WATER(shader(new WaterDistortionShader())
            .cacheSize(30)),
    BREEZE(shader(new WaterDistortionShader(2, 0.25))
            .duration(ofSeconds(4))
            .cacheSize(30)),
    GRAYSCALE(shader(new GrayscaleShader())),
    FLASHING(shader(new ColorizeShader(Color.WHITE))
            .ease(Ease.SINE_IN_OUT).
            duration(ofMillis(250))
            .cacheSize(10));


    private final Asset<ShaderOptions> options;

    ShaderBundle(final ShaderOptions options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderOptions> asset() {
        return options;
    }
}
