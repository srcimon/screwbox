package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.util.Random;

//TODO is this really chromatic abberation?
public class ChromaticAbberationShader extends Shader {

    //TODO initialize via constructor
Random random = new Random();
    double v ;
    double v1 ;
    double v2 ;
    double v3;
    public ChromaticAbberationShader(Duration duration) {
        super("chromatic-abberation");
         v = random.nextDouble(-0.3, 0.3);
         v1 = random.nextDouble(-0.3, 0.3);
         v2 = random.nextDouble(-0.3, 0.3);
         v3 = random.nextDouble(-0.3, 0.3);
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var result = ImageOperations.cloneEmpty(source);
        Graphics2D graphics = (Graphics2D)result.getGraphics();

        double x = Math.sin(20*progress.value()) * 2;
        double y = Math.sin(20*progress.value())* 2.5;
        double x2 = Math.sin(15*progress.value() ) * -2;
        double y2 = Math.sin(15*progress.value())* -2.5;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.10f+ progress.value() / 20.0)));
        graphics.drawImage(source, (int)x, (int)y, null);
        graphics.drawImage(source, (int)x2, (int)y2, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.20f+ -progress.value() / 20.0)));
        graphics.drawImage(source, (int)x, (int)y2, null);
        graphics.drawImage(source, (int)x2, (int)y2, null);
        graphics.dispose();
        return result;
    }
}
