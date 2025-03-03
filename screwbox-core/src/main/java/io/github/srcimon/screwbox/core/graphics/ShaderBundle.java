package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.shader.ChromaticAbberationShader;
import io.github.srcimon.screwbox.core.graphics.shader.IrisShotShader;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import io.github.srcimon.screwbox.core.graphics.shader.GrayscaleShader;
import io.github.srcimon.screwbox.core.graphics.shader.InvertColorShader;
import io.github.srcimon.screwbox.core.graphics.shader.OutlineShader;
import io.github.srcimon.screwbox.core.graphics.shader.DistortionShader;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.graphics.ShaderSetup.combinedShader;
import static io.github.srcimon.screwbox.core.graphics.ShaderSetup.shader;

/**
 * A list of predefined {@link ShaderSetup shader setups} for easy use.
 *
 * @see <a href="https://screwbox.dev/docs/reference/shaders/">Documentation</a>
 * @since 2.15.0
 */
public enum ShaderBundle implements AssetBundle<ShaderSetup> {

    INVERT_COLORS(shader(new InvertColorShader())),
    BREEZE(shader(new DistortionShader(2, 0, 0.25)).duration(ofSeconds(2))),
    GRAYSCALE(shader(new GrayscaleShader())),
    ALARMED(shader(new ColorizeShader(Color.RED)).ease(Ease.SINE_IN_OUT).duration(ofMillis(250))),
    HURT(shader(new ColorizeShader(Color.WHITE)).ease(Ease.SINE_IN_OUT).duration(ofMillis(500))),
    WATER(shader(new DistortionShader())),
    SEAWATER(shader(new DistortionShader(2, 0, 0.5))),
    OUTLINE(shader(new OutlineShader(Color.BLACK))),
    IRIS_SHOT(shader(new IrisShotShader()).ease(Ease.SINE_IN_OUT)),
    SELECTED(shader(new OutlineShader(Color.WHITE, true)).ease(Ease.SINE_IN_OUT).duration(ofMillis(500))),
    CHROMATIC_ABBERATION(combinedShader(
            new ColorizeShader(Color.DARK_BLUE, Color.RED), new ChromaticAbberationShader(Duration.ofSeconds(1))
    ).ease(Ease.LINEAR_IN).duration(Duration.ofSeconds(2)));

    private final Asset<ShaderSetup> options;

    ShaderBundle(final ShaderSetup options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderSetup> asset() {
        return options;
    }
}
