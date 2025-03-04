package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;

public class EaseReplaceShader extends Shader {

    private final Ease ease;
    private final Shader subShader;

    public EaseReplaceShader(Ease ease, Shader subShader) {
        super("change-ease-shader-%s-%s".formatted(ease, subShader.cacheKey()));
        this.ease = ease;
        this.subShader = subShader;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        return subShader.apply(source, ease.applyOn(progress));
    }
}
