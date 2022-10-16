package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// TODO:COnfigurable!!!
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
        final BufferedImage result = (BufferedImage) ImageUtil.applyFilter(image, new InvertAlphaFilter());
//TODO: configurable !!!!! RADIUS and off
//        return Sprite.fromImage(result);
        int radius = 2;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage filter = op.filter(result, null);
        graphics.dispose();
        // TODO: exception on second call
        return Sprite.fromImage(filter);
    }

    public double resolution() {
        return resolution;
    }
}
