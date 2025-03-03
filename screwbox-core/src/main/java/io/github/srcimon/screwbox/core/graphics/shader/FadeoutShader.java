package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;

public class FadeoutShader extends Shader {

    protected FadeoutShader() {
        super("FadeoutShader");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        var xxx ImageOperations.createEmptyClone(source);
    }
}
