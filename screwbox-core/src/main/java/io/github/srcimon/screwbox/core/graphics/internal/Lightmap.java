package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class Lightmap {

    record PointLight(Offset position, int radius, List<Offset> area,
                      io.github.srcimon.screwbox.core.graphics.Color color) {
    }

    record SpotLight(Offset position, int radius, io.github.srcimon.screwbox.core.graphics.Color color) {
    }

    private static final java.awt.Color FADE_TO_COLOR = AwtMapper.toAwtColor(io.github.srcimon.screwbox.core.graphics.Color.TRANSPARENT);
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;
    private final float[] fractions;

    private final List<PointLight> pointLights = new ArrayList<>();
    private final List<SpotLight> spotLights = new ArrayList<>();
    private final List<WindowBounds> fullBrigthnessAreas = new ArrayList<>();

    public Lightmap(final Size size, final int resolution, Percent lightFade) {
        this.image = new BufferedImage(
                size.width() / resolution + 1, // to avoid glitches add 1
                size.height() / resolution + 1, // to avoid glitches add 1
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
        final float falloffValue = (float) MathUtil.clamp(0.1f, lightFade.invert().value(), 0.99f);
        this.fractions = new float[]{falloffValue, 1f};
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
        graphics.setColor(AwtMapper.toAwtColor(io.github.srcimon.screwbox.core.graphics.Color.BLACK));
        applyOpacityConfig(io.github.srcimon.screwbox.core.graphics.Color.BLACK);
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
        return ImageUtil.applyFilter(image, new InvertLightmapImageWithMinOpacityFilter());
    }

    private void applyOpacityConfig(final io.github.srcimon.screwbox.core.graphics.Color color) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) color.opacity().value()));
    }


    private RadialGradientPaint radialPaint(final Offset position, final int radius, final io.github.srcimon.screwbox.core.graphics.Color color) {
        final var usedRadius = Math.max(radius, resolution);
        final var colors = new java.awt.Color[]{AwtMapper.toAwtColor(color.opacity(1)), FADE_TO_COLOR};

        return new RadialGradientPaint(
                position.x() / (float) resolution,
                position.y() / (float) resolution,
                usedRadius / (float) resolution,
                fractions, colors);
    }

}
