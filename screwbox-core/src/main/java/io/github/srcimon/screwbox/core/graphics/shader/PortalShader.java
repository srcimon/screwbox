package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

public class PortalShader extends Shader {

    public static void main(String[] args) {
        var x = ScrewBox.createEngine("xx");
        ShaderSetup shader = ShaderSetup.shader(new PortalShader()).ease(Ease.SINE_IN_OUT);

        x.environment().enableAllFeatures()
                .addEntity(new TransformComponent(),
                        new RenderComponent(SpriteBundle.BOX_STRIPED, SpriteDrawOptions.scaled(3).shaderSetup(shader)));
        x.start();
    }

    protected PortalShader() {
        super("portal");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var sourceImage = toBufferedImage(source);
        var drawImage = ImageOperations.createSameSizeImage(source);
        Graphics2D graphics = (Graphics2D) drawImage.getGraphics();
        final int width = sourceImage.getWidth();
        final int height = sourceImage.getWidth();
        final int centerX = width / 2;
        final int centerY = height / 2;
        int travelX = (int) (progress.value() * centerX) ;
        int travelY = (int) (progress.value() * centerY);
        var t = new AffineTransform();
        t.translate(travelX, travelY);
        t.scale(progress.invert().value(), progress.invert().value());
        graphics.drawImage(sourceImage, t, null);
        graphics.dispose();
        return drawImage;
    }
}
