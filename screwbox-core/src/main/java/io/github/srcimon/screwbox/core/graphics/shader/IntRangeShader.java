package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Dynamically creates a {@link Shader} matching the specified range depending on current progress.
 *
 * @see DoubleRangeShader
 * @since 2.18.0
 */
public class IntRangeShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ShaderCreator subShader;
    private final int from;
    private final int to;

    @FunctionalInterface
    public interface ShaderCreator extends Serializable {
        Shader create(int value);
    }

    public IntRangeShader(final int from, final int to, final ShaderCreator subShader) {
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
