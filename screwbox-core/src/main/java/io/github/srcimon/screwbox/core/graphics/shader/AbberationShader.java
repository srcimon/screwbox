package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AbberationShader extends Shader {

    //TODO initialize via constructor

    public AbberationShader() {
        super("abberation");
    }

//    double x = Pixelperfect.value(Math.sin(20 * progress.value()) );
//    double y = Pixelperfect.value(Math.sin(20 * progress.value()) * 3);
//    double x2 = Pixelperfect.value(Math.sin(15 * progress.value()) * -2);
//    double y2 = Pixelperfect.value(Math.sin(15 * progress.value()) * -3);

    @Override
    public Image apply(Image source, Percent progress) {
        int added = 8;
        final var result = new BufferedImage(source.getWidth(null) + added, source.getHeight(null) + added, BufferedImage.TYPE_INT_ARGB);
        int xo = added / 2;
        Graphics2D graphics = (Graphics2D) result.getGraphics();
        double x =Math.sin(10 * progress.value()) *3;
        double y = Math.sin(20 * progress.value()) * 3;
        double x2 = Math.sin(15 * progress.value()) * -2;
        double y2 = Math.sin(15 * progress.value()) * -3;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.10f + progress.value() / 20.0)));
        graphics.drawImage(source, xo+(int) x, xo+(int) y, null);
        graphics.drawImage(source, xo+(int) x2, xo+(int) y2, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.20f + -progress.value() / 20.0)));
        graphics.drawImage(source, xo+(int) x, xo+(int) y2, null);
        graphics.drawImage(source, xo+(int) x2, xo+(int) y2, null);
        graphics.dispose();
        return result;
    }
}
