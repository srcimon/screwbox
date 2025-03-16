package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.io.Serial;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Combines multiple shaders to a single one.
 *
 * @since 2.15.0
 */
public class CombinedShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Shader> shaders;

    /**
     * Creates a new instance using the specified shaders.
     */
    public CombinedShader(final Shader... shaders) {
        super("combined-shader-" + Stream.of(shaders).map(Shader::cacheKey).collect(joining("-")), Stream.of(shaders).anyMatch(Shader::isAnimated));
        Validate.min(shaders.length, 2, "combined shader needs at least two sub shaders");
        this.shaders = List.of(shaders);
    }

    @Override
    public Image apply(final Image source, Percent progress) {
        Image lastResult = source;
        for (final var shader : shaders) {
            lastResult = shader.apply(lastResult, progress);
        }
        return lastResult;
    }
}
