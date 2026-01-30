package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.MathUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serial;

public class FoliageShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Percent strength;

    public FoliageShader(final Percent strength) {
        super("FoliageShader-" + strength.value());
        this.strength = strength;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var result = ImageOperations.createEmptyImageOfSameSize(source);
        final var graphics = result.createGraphics();
        final var transform = new AffineTransform();
        final var sin = MathUtil.fastSin(progress.value() * 2 * Math.PI);
        transform.translate(0, source.getHeight(null));
        transform.shear(sin * strength.value(), 0);
        transform.translate(0, -source.getHeight(null));
        graphics.setTransform(transform);
        graphics.drawImage(source, transform, null);
        graphics.dispose();
        return result;
    }
}
