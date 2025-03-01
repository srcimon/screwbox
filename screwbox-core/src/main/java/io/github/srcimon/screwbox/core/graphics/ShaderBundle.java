package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.graphics.shader.GrayscaleShader;
import io.github.srcimon.screwbox.core.graphics.shader.InvertColorShader;
import io.github.srcimon.screwbox.core.graphics.shader.OutlineShader;
import io.github.srcimon.screwbox.core.graphics.shader.WaterDistortionShader;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.graphics.ShaderSetup.shader;

/**
 * A list of predefined {@link ShaderSetup shader setups} for easy use.
 *
 * @see <a href="https://screwbox.dev/docs/reference/shaders/">Documentation</a>
 *
 * @since 2.15.0
 */
public enum ShaderBundle implements AssetBundle<ShaderSetup> {

    INVERT_COLORS(shader(new InvertColorShader())),
    BREEZE(shader(new WaterDistortionShader(2, 0.25)).duration(ofSeconds(2))),
    GRAYSCALE(shader(new GrayscaleShader())),
    FLASHING_RED(shader(new ColorizeShader(Color.RED)).ease(Ease.SINE_IN_OUT).duration(ofMillis(250))),
    FLASHING_WHITE(shader(new ColorizeShader(Color.WHITE)).ease(Ease.SINE_IN_OUT).duration(ofMillis(250))),
    WATER(shader(new WaterDistortionShader())),
    OUTLINE_BLACK(shader(new OutlineShader(Color.BLACK)));

    private final Asset<ShaderSetup> options;

    ShaderBundle(final ShaderSetup options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderSetup> asset() {
        return options;
    }
}
