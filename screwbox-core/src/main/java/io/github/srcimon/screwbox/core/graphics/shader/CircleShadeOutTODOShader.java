package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

public class CircleShadeOutTODOShader extends Shader {

    public static void main(String[] args) {
        var x = ScrewBox.createEngine("xx");
        ShaderSetup shader = ShaderSetup.shader(new CircleShadeOutTODOShader()).ease(Ease.SINE_IN_OUT).duration(Duration.ofSeconds(4)).offset(Time.now());

        x.environment().enableAllFeatures()
                .addEntity(new TransformComponent(),
                        new RenderComponent(SpriteBundle.BOX_STRIPED.get().addBorder(2, Color.TRANSPARENT), SpriteDrawOptions.scaled(3).shaderSetup(shader)));
        x.start();
    }

    protected CircleShadeOutTODOShader() {
        super("portal");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var sourceImage = toBufferedImage(source);
//        var drawImage = ImageOperations.createEmptyClone(source);
//        Graphics2D graphics = (Graphics2D) drawImage.getGraphics();
//        final int width = sourceImage.getWidth();
//        final int height = sourceImage.getWidth();
//        final int centerX = width / 2;
//        final int centerY = height / 2;
//        int travelX = (int) (progress.value() * centerX);
//        int travelY = (int) (progress.value() * centerY);
//        var transform = new AffineTransform();
//        transform.translate(travelX, travelY);
//        transform.scale(progress.invert().value(), progress.invert().value());
//        graphics.drawImage(sourceImage, transform, null);
//        graphics.dispose();

        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;
        double maxDistanceToCenter = Math.sqrt(centerX * centerX + centerY * centerY);

        final var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                int distanceX = Math.abs(centerX - x);
                int distanceY = Math.abs(centerY - y);
                double distanceToCenter = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if (distanceToCenter < maxDistanceToCenter * progress.value()) {
                    return sourceImage.getRGB(x, y);
                }
                return 0;
            }
        };
        return ImageOperations.applyFilter(sourceImage, filter);

//        return drawImage;
    }
}
