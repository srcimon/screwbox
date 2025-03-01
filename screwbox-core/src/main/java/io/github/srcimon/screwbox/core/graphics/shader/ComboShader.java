package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.util.List;

import static java.util.stream.Collectors.joining;


public class ComboShader extends Shader {

    private final List<Shader> shaders;

    //TODO document
    public ComboShader(final Shader... shaders) {
        this(List.of(shaders));
    }

    public ComboShader(final List<Shader> shaders) {
        super("combo-shader-" + shaders.stream().map(Shader::cacheKey).collect(joining("-")), shaders.stream().anyMatch(Shader::isAnimated));
        Validate.min(shaders.size(), 2, "combo shader needs at least 2 sub shaders");
        this.shaders = shaders;
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
