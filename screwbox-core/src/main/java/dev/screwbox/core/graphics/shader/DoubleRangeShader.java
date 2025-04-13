package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Dynamically creates a {@link Shader} matching the specified range depending on current progress.
 *
 * @see IntRangeShader
 * @since 2.18.0
 */
public class DoubleRangeShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ShaderCreator subShader;
    private final double from;
    private final double to;

    @FunctionalInterface
    public interface ShaderCreator extends Serializable {
        Shader create(double value);
    }

    public DoubleRangeShader(final double from, final double to, final ShaderCreator subShader) {
        super("IntRangeShader-%s-%s-%s".formatted(from, to, subShader.create(from).cacheKey()), true);
        this.subShader = subShader;
        this.from = from;
        this.to = to;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var rangeValue = progress.rangeValue(from, to);
        return subShader.create(rangeValue).apply(source, progress);
    }
}
