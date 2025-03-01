package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;

public class SizeIncreaseShader extends Shader {

    private final int increase;

    public SizeIncreaseShader(final int increase) {
        super("size-expansion-%s".formatted(increase), false);
        Validate.range(increase, 1, 32, "only size increase from 1 to 32 is supported");
        this.increase = increase;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageUtil.addBorder(source, increase, Color.TRANSPARENT);
    }
}
