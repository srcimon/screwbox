package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.InvertAlphaFilter;
import de.suzufa.screwbox.core.graphics.Offset;

public class Lightmap implements AutoCloseable {

    private static final java.awt.Color FADE_TO_COLOR = toAwtColor(Color.TRANSPARENT);
    private static final float[] FRACTIONS = new float[] { 0.1f, 1f };

    private BufferedImage image;
    private Graphics2D graphics;
    private final int resolution;

    public Lightmap(final Dimension size, final int resolution, final boolean isAntialiased) {
        this.image = new BufferedImage(
                size.width() / resolution + 1, // to avoid glitches add 1
                size.height() / resolution + 1, // to avoid glitches add 1
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
        if (isAntialiased) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    public void addPointLight(final Offset position, final int range, final List<Offset> area, final Color color) {
        final Polygon polygon = new Polygon();
        for (final var node : area) {
            polygon.addPoint(node.x() / resolution, node.y() / resolution);
        }

        final RadialGradientPaint paint = radialPaint(position, range, color);
        graphics.setPaint(paint);
        applyOpacityConfig(color);
        graphics.fillPolygon(polygon);
    }

    public void addSpotLight(final Offset position, final int range, final Color color) {
        final RadialGradientPaint paint = radialPaint(position, range, color);
        graphics.setPaint(paint);
        applyOpacityConfig(color);
        graphics.fillOval(
                position.x() / resolution - range / resolution / 2,
                position.y() / resolution - range / resolution / 2,
                range / resolution,
                range / resolution);
    }

    public void addLensFlare(Offset origin, Offset target, int size, Color color) {
        graphics.setColor(toAwtColor(color));

        int xStep = (target.x() - origin.x()) / 4 / resolution;
        int yStep = (target.y() - origin.y()) / 4 / resolution;
        var sizes = List.of(1.0, 1.2, 0.3, 0.5, 1.0, 0.4, 2.0);

        for (int i = 1; i < 6; i++) {
            applyOpacityConfig((float) (0.07 * (2 - sizes.get(i))));
            int sizeCurrent = (int) (sizes.get(i) * size) / resolution;
            graphics.fillOval(
                    origin.x() / resolution + xStep * i - sizeCurrent / 2,
                    origin.y() / resolution + yStep * i - sizeCurrent / 2, sizeCurrent, sizeCurrent);
        }

    }

    public void invertImage() {
        graphics.dispose();
        image = (BufferedImage) ImageUtil.applyFilter(image, new InvertAlphaFilter());
        graphics = (Graphics2D) image.getGraphics();
    }

    public BufferedImage image() {
        return image;
    }

    private void applyOpacityConfig(final Color color) {
        applyOpacityConfig(color.opacity().valueFloat());
    }

    private void applyOpacityConfig(float value) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));
    }

    public int resolution() {
        return resolution;
    }

    @Override
    public void close() {
        graphics.dispose();
    }

    private RadialGradientPaint radialPaint(final Offset position, final int range, final Color color) {
        final var colors = new java.awt.Color[] { toAwtColor(color), FADE_TO_COLOR };

        return new RadialGradientPaint(
                position.x() / (float) resolution,
                position.y() / (float) resolution,
                range / resolution / 2f,
                FRACTIONS, colors);
    }

}
