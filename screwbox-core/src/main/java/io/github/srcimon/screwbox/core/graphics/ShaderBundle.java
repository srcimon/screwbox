package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.shader.*;

import java.util.Set;

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
    GREYSCALE(shader(new GreyscaleShader())),
    ALARMED(shader(new ColorizeShader(Color.RED)).ease(Ease.SINE_IN_OUT).duration(ofMillis(250))),
    HURT(shader(new ColorizeShader(Color.WHITE)).ease(Ease.SINE_IN_OUT).duration(ofMillis(500))),
    WATER(shader(new DistortionShader())),
    SEAWATER(shader(new DistortionShader(2, 0, 0.5))),
    OUTLINE(shader(new OutlineShader(Color.BLACK))),
    IRIS_SHOT(shader(new IrisShotShader()).ease(Ease.SINE_IN_OUT)),
    FOLIAGE(combinedShader(new SizeIncreaseShader(2, 0), new FoliageShader(0.1))
            .randomOffset()
            .duration(Duration.ofSeconds(2))),
    SELECTED(shader(new OutlineShader(Color.WHITE, true)).ease(Ease.SINE_IN_OUT).duration(ofMillis(500))),
    CHROMATIC_ABERRATION(combinedShader(
            new EaseReplaceShader(Ease.SINE_IN_OUT, new ColorizeShader(Color.DARK_BLUE, Color.RED)),
            new AberrationShader())
            .ease(Ease.LINEAR_IN)
            .duration(Duration.ofSeconds(2))),
    PIXELATE(shader(new IntRangeShader(1, 8, PixelateShader::new)).ease(Ease.SINE_IN_OUT).duration(Duration.ofSeconds(2))),
    SILHOUETTE(shader(new SilhouetteShader(Color.BLACK))),
    DISSOLVE(combinedShader(
            new DissolveShader(),
            new OutlineShader(Color.WHITE.opacity(0.5)),
            new ColorizeShader(Color.hex("#37b9de")))
            .duration(Duration.ofSeconds(2))),
    SUNBURN(shader(new ColorPaletteShader(Set.of(
            Color.hex("#003049"),
            Color.hex("#d62828"),
            Color.hex("#f77f00"),
            Color.hex("#fcbf49"),
            Color.hex("#eae2b7"))))),
    GAMEBOY(shader(new ColorPaletteShader(Set.of(
            Color.hex("#9bbc0f"),
            Color.hex("#8bac0f"),
            Color.hex("#306230"),
            Color.hex("#0f380f")))));

    private final Asset<ShaderSetup> options;

    ShaderBundle(final ShaderSetup options) {
        this.options = Asset.asset(() -> options);
    }

    @Override
    public Asset<ShaderSetup> asset() {
        return options;
    }
}
