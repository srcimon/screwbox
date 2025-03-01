package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Combines multiple shaders to a single one.
 *
 * @since 2.15.0
 */
public class CombinedShader extends Shader {

    private final List<Shader> shaders;

    /**
     * Creates a new instance using the specified shaders.
     */
    public CombinedShader(final Shader... shaders) {
        super("combined-shader-" + Stream.of(shaders).map(Shader::cacheKey).collect(joining("-")), Stream.of(shaders).anyMatch(Shader::isAnimated));
        Validate.min(shaders.length, 2, "combo shader needs at least 2 sub shaders");
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
