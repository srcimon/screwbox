package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.graphics.shader.GrayscaleShader;
import io.github.srcimon.screwbox.core.graphics.shader.WaterDistortionShader;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderSetup.shader;

public enum ShaderBundle implements AssetBundle<ShaderSetup> {

    WATER(shader(new WaterDistortionShader())),
    BREEZE(shader(new WaterDistortionShader(2, 0.25))
            .duration(ofSeconds(4))),
    GRAYSCALE(shader(new GrayscaleShader())),
    FLASHING_WHITE(shader(new ColorizeShader(Color.WHITE))
            .ease(Ease.SINE_IN_OUT).
            duration(ofMillis(250))),
    FLASHING_RED(shader(new ColorizeShader(Color.RED))
            .ease(Ease.SINE_IN_OUT).
            duration(ofMillis(250)));


    private final Asset<ShaderSetup> options;

    ShaderBundle(final ShaderSetup options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderSetup> asset() {
        return options;
    }
}
