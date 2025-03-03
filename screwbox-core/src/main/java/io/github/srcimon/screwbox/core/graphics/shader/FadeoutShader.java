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
        var result =  ImageOperations.createEmptyClone(source);
        final var graphics = (Graphics2D)result.getGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) progress.invert().value()));
        graphics.drawImage(source, 0,0, null);
        graphics.dispose();
        return result;
    }
}
