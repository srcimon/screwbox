package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serial;

public class FoliageShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final double strength;

    public FoliageShader(final double strength) {
        super("FoliageShader-%s".formatted(strength));
        Validate.range(strength, 0, 1, "strength must be in range 0 to 1");
        this.strength = strength;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var result = ImageOperations.cloneEmpty(source);
        final var graphics = (Graphics2D) result.getGraphics();
        final var transform = new AffineTransform();
        final var sin = Math.sin(progress.value() * 2 * Math.PI);
        transform.translate(0, source.getHeight(null));
        transform.shear(sin * strength, 0);
        transform.translate(0, -source.getHeight(null));
        graphics.setTransform(transform);
        graphics.drawImage(source, transform, null);
        graphics.dispose();
        return result;
    }
}
