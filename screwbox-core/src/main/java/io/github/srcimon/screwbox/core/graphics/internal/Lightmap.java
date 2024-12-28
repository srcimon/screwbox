package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.filter.InvertImageMinOpacityFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.AlphaComposite.SRC_OVER;

class Lightmap {

    record PointLight(Offset position, int radius, List<Offset> area, Color color) {
    }

    record SpotLight(Offset position, int radius, Color color) {
    }

    private static final java.awt.Color FADE_TO_COLOR = AwtMapper.toAwtColor(Color.TRANSPARENT);
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;
    private final float[] fractions;

    private final List<PointLight> pointLights = new ArrayList<>();
    private final List<SpotLight> spotLights = new ArrayList<>();
    private final List<ScreenBounds> fullBrigthnessAreas = new ArrayList<>();
    private final List<ScreenBounds> orthographicWalls = new ArrayList<>();

    public Lightmap(final Size size, final int resolution, final Percent lightFade) {
        this.image = new BufferedImage(
                Math.max(1, size.width() / resolution),
                Math.max(1, size.height() / resolution),
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
        final double value = lightFade.invert().value();
        final float falloffValue = (float) Math.clamp(value, 0.1f, 0.99f);
        this.fractions = new float[]{falloffValue, 1f};
    }

    public void addOrthographicWall(ScreenBounds screenBounds) {
        orthographicWalls.add(screenBounds);
    }


    public void addFullBrightnessArea(final ScreenBounds fullBrightnessArea) {
        fullBrigthnessAreas.add(fullBrightnessArea);
    }

    public void addPointLight(final PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public void addSpotlight(final SpotLight spotLight) {
        spotLights.add(spotLight);
    }

    private void renderFullBrightnessArea(final ScreenBounds bounds) {
        graphics.setColor(AwtMapper.toAwtColor(Color.BLACK));
        applyOpacityConfig(Color.BLACK);
        graphics.fillRect(bounds.offset().x() / resolution,
                bounds.offset().y() / resolution,
                bounds.width() / resolution,
                bounds.height() / resolution);
    }

    private void renderPointlight(final PointLight pointLight) {
        final Polygon polygon = new Polygon();
        for (final var node : pointLight.area()) {
            polygon.addPoint(node.x() / resolution, node.y() / resolution);
        }

        final RadialGradientPaint paint = radialPaint(pointLight.position(), pointLight.radius(), pointLight.color());
        applyOpacityConfig(pointLight.color());
        graphics.setPaint(paint);
        graphics.fillPolygon(polygon);
    }

    private void renderSpotlight(final SpotLight spotLight) {
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
            renderPointlight(pointLight);
        }
        for (final var spotLight : spotLights) {
            renderSpotlight(spotLight);
        }
        for (final var fullBrigthnessArea : fullBrigthnessAreas) {
            renderFullBrightnessArea(fullBrigthnessArea);
        }
        graphics.dispose();
        return ImageUtil.applyFilter(image, new InvertImageMinOpacityFilter());
    }

    private void applyOpacityConfig(final io.github.srcimon.screwbox.core.graphics.Color color) {
        graphics.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) color.opacity().value()));
    }


    private RadialGradientPaint radialPaint(final Offset position, final int radius, final Color color) {
        final var usedRadius = Math.max(radius, resolution);
        final var colors = new java.awt.Color[]{AwtMapper.toAwtColor(color.opacity(1)), FADE_TO_COLOR};

        return new RadialGradientPaint(
                position.x() / (float) resolution,
                position.y() / (float) resolution,
                usedRadius / (float) resolution,
                fractions, colors);
    }
}