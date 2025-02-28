package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serializable;

public class ComboShader extends Shader {

    private final Shader second;
    private final Shader first;

    //TODO document
    public ComboShader(Shader first, Shader second) {
        super("combo-shader-" + first.cacheKey() + "-" + second, true);//TODO true only if any is animated
        this.first = first;
        this.second = second;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        return second.apply(first.apply(source, progress), progress);
    }
}
