package io.github.srcimon.screwbox.playground.scene.world;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class FoliageShader extends Shader {

    private final double strength;

    public FoliageShader(final double strength) {
        super("FoliageShader-%s".formatted(strength));
        Validate.range(strength, 0, 1, "strength must be in range 0 to 1");
        this.strength = strength;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var result = ImageOperations.cloneEmpty(source);
        final var graphics = (Graphics2D)result.getGraphics();
        AffineTransform transform = new AffineTransform();
        final var sin = Math.sin(progress.value()*2 * Math.PI);
        transform.translate(0,source.getHeight(null));
        transform.shear(sin*strength ,0);
        transform.translate(0,-source.getHeight(null));
        graphics.setTransform(transform);
        graphics.drawImage(source, transform, null);
        graphics.dispose();
        return result;
    }
}
