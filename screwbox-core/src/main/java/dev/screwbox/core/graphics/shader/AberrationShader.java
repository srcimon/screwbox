package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.io.Serial;

import static java.awt.AlphaComposite.SRC_OVER;

/**
 * Creates an aberration displacement effect.
 *
 * @since 2.17.0
 */
public class AberrationShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int SIZE_INCREASE = 8;
    private static final int OFFSET_CORRECT = SIZE_INCREASE / 2;

    public AberrationShader() {
        super("aberration-shader");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var result = ImageOperations.createImage(Size.of(
                source.getWidth(null) + SIZE_INCREASE,
                source.getHeight(null) + SIZE_INCREASE));

        final Graphics2D graphics = (Graphics2D) result.getGraphics();
        final int x = (int) (Math.sin(10 * progress.value()) * 3);
        final int y = (int) (Math.sin(20 * progress.value()) * 5);
        final int x2 = (int) (Math.sin(20 * progress.value()) * -5);
        final int y2 = (int) (Math.sin(10 * progress.value()) * -3);

        graphics.setComposite(AlphaComposite.getInstance(SRC_OVER, 0.10f));
        drawImage(source, graphics, x, y);
        drawImage(source, graphics, x2, y2);

        graphics.setComposite(AlphaComposite.getInstance(SRC_OVER, 0.20f));
        drawImage(source, graphics, x, y2);
        drawImage(source, graphics, x2, y2);

        graphics.dispose();
        return result;
    }

    private static void drawImage(final Image source, final Graphics2D graphics, final int x, final int y) {
        graphics.drawImage(source, OFFSET_CORRECT + x, OFFSET_CORRECT + y, null);
    }
}
