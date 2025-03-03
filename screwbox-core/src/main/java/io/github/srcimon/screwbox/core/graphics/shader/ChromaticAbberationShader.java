package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;

//TODO is this really chromatic abberation?
public class ChromaticAbberationShader extends Shader {

    //TODO initialize via constructor

    public ChromaticAbberationShader() {
        super("chromatic-abberation");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var result = ImageOperations.cloneEmpty(source);
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        double x = Math.sin(progress.value() / 0.1) * 4;
        double y = Math.sin(progress.value() / 0.15)* 5;
        double x2 = Math.sin(progress.value() / 0.1) * -4;
        double y2 = Math.sin(progress.value() / 0.15)* -5;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.1f+ progress.value() / 10.0)));
        graphics.drawImage(source, (int)x, (int)y, null);
        graphics.drawImage(source, (int)x2, (int)y2, null);
        graphics.dispose();
        return result;
    }
}
