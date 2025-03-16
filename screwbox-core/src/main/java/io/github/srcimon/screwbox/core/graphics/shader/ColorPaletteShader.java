package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serial;
//TODO document
//TODO implement
public class ColorPaletteShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    protected ColorPaletteShader() {
        super("ColorPaletteShader");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        return source;
    }
}
