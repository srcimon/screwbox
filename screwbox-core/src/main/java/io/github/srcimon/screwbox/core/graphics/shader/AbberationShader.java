package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AbberationShader extends Shader {

    private static final int SIZE_INCREASE = 8;
    private static final int OFFSET_CORRECT = SIZE_INCREASE / 2;
    //TODO initialize via constructor

    public AbberationShader() {
        super("abberation");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var result = new BufferedImage(
                source.getWidth(null) + SIZE_INCREASE,
                source.getHeight(null) + SIZE_INCREASE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) result.getGraphics();
        double x = Math.sin(10 * progress.value()) * 3;
        double y = Math.sin(20 * progress.value()) * 5;
        double x2 = Math.sin(20 * progress.value()) * -5;
        double y2 = Math.sin(10 * progress.value()) * -3;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.10f + progress.value() / 20.0)));
        graphics.drawImage(source, OFFSET_CORRECT + (int) x, OFFSET_CORRECT + (int) y, null);
        graphics.drawImage(source, OFFSET_CORRECT + (int) x2, OFFSET_CORRECT + (int) y2, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.20f + -progress.value() / 20.0)));
        graphics.drawImage(source, OFFSET_CORRECT + (int) x, OFFSET_CORRECT + (int) y2, null);
        graphics.drawImage(source, OFFSET_CORRECT + (int) x2, OFFSET_CORRECT + (int) y2, null);
        graphics.dispose();
        return result;
    }
}
