package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AbberationShader extends Shader {

    //TODO initialize via constructor

    public AbberationShader() {
        super("abberation");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var result = new BufferedImage(source.getWidth(null) + 16, source.getHeight(null) + 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) result.getGraphics();
        double x = Math.sin(20 * progress.value()) * 2;
        double y = Math.sin(20 * progress.value()) * 3;
        double x2 = Math.sin(15 * progress.value()) * -2;
        double y2 = Math.sin(15 * progress.value()) * -3;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.10f + progress.value() / 20.0)));
        graphics.drawImage(source, (int) x, (int) y, null);
        graphics.drawImage(source, (int) x2, (int) y2, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.20f + -progress.value() / 20.0)));
        graphics.drawImage(source, (int) x, (int) y2, null);
        graphics.drawImage(source, (int) x2, (int) y2, null);
        graphics.dispose();
        return result;
    }
}
