package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Customize the drawing of lines using textures.
 *
 * @param drawOrder order of this drawing task in comparison to others
 * @param scale     scale of the used texture
 * @param shader    specify the {@link ShaderBundle} for drawing
 * @param opacity   specify the opacity of the texture
 * @see Canvas#drawLine(Offset, Offset, Sprite, TextureLineDrawOptions)
 * @since 3.38.0
 */
public record TextureLineDrawOptions(int drawOrder, double scale, ShaderSetup shader, Percent opacity) {

    public TextureLineDrawOptions {
        Objects.requireNonNull(opacity, "opacity must not be null");
        Validate.positive(scale, "scale must not be negative");
    }

    /**
     * Creates options for a scaled texture.
     */
    public static TextureLineDrawOptions scaled(final double scale) {
        return new TextureLineDrawOptions(0, scale, null, Percent.max());
    }

    /**
     * Creates options for an unscaled texture.
     */
    public static TextureLineDrawOptions originalSize() {
        return scaled(1);
    }

    /**
     * Sets order of this drawing task in comparison to others.
     */
    public TextureLineDrawOptions drawOrder(final int drawOrder) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }

    /**
     * Sets order of this drawing task in comparison to others.
     */
    public TextureLineDrawOptions shader(final Supplier<ShaderSetup> shader) {
        return shader(shader.get());
    }

    /**
     * Sets the {@link ShaderBundle} for drawing.
     */
    public TextureLineDrawOptions shader(final ShaderSetup shader) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }

    /**
     * Sets the opacity for drawing.
     */
    public TextureLineDrawOptions opacity(final Percent opacity) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }
}
