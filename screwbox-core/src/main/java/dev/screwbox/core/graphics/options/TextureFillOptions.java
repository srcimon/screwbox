package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.Validate;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Customize the drawing of filling the {@link Screen} with {@link Sprite}s.
 *
 * @param offset      the {@link Offset} used to fill the {@link Screen}
 * @param scale       the scale of the {@link Sprite}s used to fill the {@link Screen}
 * @param opacity     the opacity used to fill the {@link Screen}
 * @param shaderSetup {@link ShaderSetup} used for drawing
 * @param drawOrder   order of this drawing task in comparison to others
 * @see Canvas#fillWith(Sprite, TextureFillOptions)
 */
public record TextureFillOptions(Offset offset, double scale, Percent opacity, ShaderSetup shaderSetup, int drawOrder) {

    public TextureFillOptions {
        Validate.positive(scale, "scale must be positive");
        requireNonNull(offset, "offset must not be null");
        requireNonNull(opacity, "opacity must not be null");
    }

    /**
     * Creates a new instance with given {@link #scale()}.
     */
    public static TextureFillOptions scale(final double scale) {
        return new TextureFillOptions(Offset.origin(), scale, Percent.max(), null, 0);
    }

    /**
     * Creates a new instance with given {@link #offset()}
     */
    public TextureFillOptions offset(final Offset offset) {
        return new TextureFillOptions(offset, scale, opacity, shaderSetup, drawOrder);
    }

    /**
     * Creates a new instance with given {@link #opacity()}.
     */
    public TextureFillOptions opacity(final Percent opacity) {
        return new TextureFillOptions(offset, scale, opacity, shaderSetup, drawOrder);
    }

    /**
     * Sets {@link ShaderSetup} that should be applied on the {@link Sprite sprites} when drawn.
     *
     * @since 2.15.0
     */
    public TextureFillOptions shaderSetup(ShaderSetup shaderSetup) {
        return new TextureFillOptions(offset, scale, opacity, shaderSetup, drawOrder);
    }

    /**
     * Sets {@link ShaderSetup} that should be applied on the {@link Sprite sprites} when drawn.
     *
     * @since 2.15.0
     */
    public TextureFillOptions shaderSetup(Supplier<ShaderSetup> shaderOptions) {
        return shaderSetup(shaderOptions.get());
    }

    /**
     * Specify the order of this drawing task in comparison to others.
     *
     * @since 3.14.0
     */
    public TextureFillOptions drawOrder(final int drawOrder) {
        return new TextureFillOptions(offset, scale, opacity, shaderSetup, drawOrder);
    }
}
