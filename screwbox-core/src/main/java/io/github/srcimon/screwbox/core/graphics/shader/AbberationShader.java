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

        final Graphics2D graphics = (Graphics2D) result.getGraphics();
        final int x = (int)Math.sin(10 * progress.value()) * 3;
        final int y = (int)Math.sin(20 * progress.value()) * 5;
        final int x2 = (int)Math.sin(20 * progress.value()) * -5;
        final int y2 = (int)Math.sin(10 * progress.value()) * -3;

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.10f + progress.value() / 20.0)));
        drawImage(source, graphics, x, y);
        drawImage(source, graphics, x2, y2);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (0.20f + -progress.value() / 20.0)));
        drawImage(source, graphics, x, y2);
        drawImage(source, graphics, x2, y2);
        graphics.dispose();
        return result;
    }

    private static void drawImage(final Image source, final Graphics2D graphics, final int x, final int y) {
        graphics.drawImage(source, OFFSET_CORRECT + x, OFFSET_CORRECT + y, null);
    }
}
