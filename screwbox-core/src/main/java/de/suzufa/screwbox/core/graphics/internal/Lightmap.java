package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.WindowBounds;

class Lightmap {

    record PointLight(Offset position, int radius, List<Offset> area, Color color) {
    }

    record SpotLight(Offset position, int radius, Color color) {
    }

    private static final java.awt.Color FADE_TO_COLOR = toAwtColor(Color.TRANSPARENT);
    private static final float[] FRACTIONS = new float[] { 0.1f, 1f };

    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;

    private final List<PointLight> pointLights = new ArrayList<>();
    private final List<SpotLight> spotLights = new ArrayList<>();
    private final List<WindowBounds> fullBrigthnessAreas = new ArrayList<>();

    public Lightmap(final Dimension size, final int resolution) {
        this.image = new BufferedImage(
                size.width() / resolution + 1, // to avoid glitches add 1
                size.height() / resolution + 1, // to avoid glitches add 1
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
    }

    public void add(final WindowBounds fullBrightnessArea) {
        fullBrigthnessAreas.add(fullBrightnessArea);
    }

    public void add(final PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public void add(final SpotLight spotLight) {
        spotLights.add(spotLight);
    }

    public void addFullBrightnessArea(final WindowBounds bounds) {
        graphics.setColor(toAwtColor(Color.BLACK));
        applyOpacityConfig(Color.BLACK);
        graphics.fillRect(bounds.offset().x() / resolution,
                bounds.offset().y() / resolution,
                bounds.size().width() / resolution,
                bounds.size().height() / resolution);
    }

    private void addPointLight(final PointLight pointLight) {
        final Polygon polygon = new Polygon();
        for (final var node : pointLight.area()) {
            polygon.addPoint(node.x() / resolution, node.y() / resolution);
        }

        final RadialGradientPaint paint = radialPaint(pointLight.position(), pointLight.radius(), pointLight.color());
        applyOpacityConfig(pointLight.color());
        graphics.setPaint(paint);
        graphics.fillPolygon(polygon);
    }

    private void addSpotLight(final SpotLight spotLight) {
        final RadialGradientPaint paint = radialPaint(spotLight.position(), spotLight.radius(), spotLight.color());
        graphics.setPaint(paint);
        applyOpacityConfig(spotLight.color());
        graphics.fillOval(
                spotLight.position().x() / resolution - spotLight.radius() / resolution,
                spotLight.position().y() / resolution - spotLight.radius() / resolution,
                spotLight.radius() / resolution * 2,
                spotLight.radius() / resolution * 2);
    }

    public BufferedImage createImage() {
        for (final var pointLight : pointLights) {
            addPointLight(pointLight);
        }
        for (final var spotLight : spotLights) {
            addSpotLight(spotLight);
        }
        for (final var fullBrigthnessArea : fullBrigthnessAreas) {
            addFullBrightnessArea(fullBrigthnessArea);
        }
        graphics.dispose();
        return (BufferedImage) ImageUtil.applyFilter(image, new InvertAlphaFilter());
    }

    private void applyOpacityConfig(final Color color) {
        applyOpacityConfig(color.opacity().valueFloat());
    }

    private void applyOpacityConfig(final float value) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));
    }

    private RadialGradientPaint radialPaint(final Offset position, final int radius, final Color color) {
        final var usedRadius = radius > resolution ? radius : resolution;
        final var colors = new java.awt.Color[] { toAwtColor(color.opacity(1)), FADE_TO_COLOR };

        return new RadialGradientPaint(
                position.x() / (float) resolution,
                position.y() / (float) resolution,
                usedRadius / resolution,
                FRACTIONS, colors);
    }

    public List<PointLight> pointLights() {
        return pointLights;
    }

}
