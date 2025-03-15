package io.github.srcimon.screwbox.playground.scene.world;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class FoliageShader extends Shader {

    protected FoliageShader() {
        super("FoliageShader");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var result = ImageOperations.cloneEmpty(source);
        final var graphics = (Graphics2D)result.getGraphics();
        AffineTransform transform = new AffineTransform();
        var sin = Math.sin(progress.value()*2 * Math.PI);
        System.out.println(sin);
        transform.translate(0,source.getHeight(null));
        transform.shear(sin ,0);
        transform.translate(0,-source.getHeight(null));
        graphics.setTransform(transform);
        graphics.drawImage(source, transform, null);
        graphics.dispose();
        return result;
    }
}
