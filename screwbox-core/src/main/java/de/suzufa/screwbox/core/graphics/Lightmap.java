package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.List;

import de.suzufa.screwbox.core.graphics.internal.ImageUtil;

//TODO: javadoc and test
public class Lightmap {

    private static final java.awt.Color FADE_TO_COLOR = toAwtColor(Color.TRANSPARENT);

    private static final float[] FRACTIONS = new float[] { 0.1f, 1f };

    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;

    public Lightmap(final Dimension size, final int resolution) {
        this.image = new BufferedImage(
                size.width() / resolution + 1, // to avoid glitches add 1
                size.height() / resolution + 1, // to avoid glitches add 1
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
    }

    public Lightmap addPointLight(final Offset position, final int range, final List<Offset> area, final Color color) {
        final Polygon polygon = new Polygon();
        for (final var node : area) {
            polygon.addPoint(node.x() / resolution, node.y() / resolution);
        }

        final var colors = new java.awt.Color[] { toAwtColor(color), FADE_TO_COLOR };

        final RadialGradientPaint paint = new RadialGradientPaint(
                position.x() / resolution,
                position.y() / resolution,
                range / resolution / 2,
                FRACTIONS, colors);
        graphics.setPaint(paint);
        graphics.fillPolygon(polygon);
        // TODO: Support for non blocked lights
//        graphics.fillOval(
//                position.x() / resolution - range / resolution / 2,
//                position.y() / resolution - range / resolution / 2,
//                range / resolution,
//                range / resolution);

        return this;
    }

    public Sprite createImage() {
        final Image result = ImageUtil.applyFilter(image, new InvertAlphaFilter());
        graphics.dispose();
        // TODO: exception on second call
        return Sprite.fromImage(result);
    }

    public double resolution() {
        return resolution;
    }
}
