package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.filter.InvertImageOpacityFilter;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.internal.renderer.FirewallRenderer;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.AlphaComposite.SRC_OVER;

class Lightmap {

    record ExpandedLight(ScreenBounds bounds, Color color) {

    }

    record PointLight(Offset position, int radius, List<Offset> area, Color color) {
    }

    record SpotLight(Offset position, int radius, Color color) {
    }

    private static final java.awt.Color FADE_TO_COLOR = AwtMapper.toAwtColor(Color.TRANSPARENT);
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;
    private final float[] fractions;
    private final Size lightMapSize;
    private final List<PointLight> pointLights = new ArrayList<>();
    private final List<SpotLight> spotLights = new ArrayList<>();
    private final List<ExpandedLight> expandedLights = new ArrayList<>();
    private final List<ScreenBounds> orthographicWalls = new ArrayList<>();

    private final Canvas lightCanvas;

    public Lightmap(final Size size, final int resolution, final Percent lightFade) {
        lightMapSize = Size.of(
                Math.max(1, size.width() / resolution),
                Math.max(1, size.height() / resolution));
        this.image = ImageOperations.createImage(lightMapSize);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
        this.graphics.setBackground(AwtMapper.toAwtColor(Color.TRANSPARENT));
        final double value = lightFade.invert().value();
        final float falloffValue = (float) Math.clamp(value, 0.1f, 0.99f);
        this.fractions = new float[]{falloffValue, 1f};

        //TODO extract to custom image canvas
        DefaultRenderer render = new DefaultRenderer();
        render.updateContext(() -> graphics);
        lightCanvas = new DefaultCanvas(new FirewallRenderer(render), new ScreenBounds(lightMapSize));
    }

    public void addOrthographicWall(ScreenBounds screenBounds) {
        orthographicWalls.add(screenBounds);
    }

    public void addExpandedLight(final ScreenBounds bounds, final Color color) {
        expandedLights.add(new ExpandedLight(bounds, color));
    }

    public void addPointLight(final PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public void addSpotlight(final SpotLight spotLight) {
        spotLights.add(spotLight);
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
        for (final var orthographicWall : orthographicWalls) {
            renderOrthographicWall(orthographicWall);
        }
        for (final var expandedLight : expandedLights) {
            renderExpandedLight(expandedLight);
        }
        graphics.dispose();
        return ImageOperations.applyFilter(image, new InvertImageOpacityFilter(), lightMapSize);
    }

    private void renderExpandedLight(final ExpandedLight light) {
        applyOpacityConfig(light.color);
        lightCanvas.drawRectangle(new ScreenBounds(
                light.bounds.offset().x() / resolution,
                light.bounds.offset().y() / resolution,
                light.bounds.width() / resolution,
                light.bounds.height() / resolution), RectangleDrawOptions.filled(light.color));
    }

    private void renderOrthographicWall(final ScreenBounds orthographicWall) {
        var lastClip = graphics.getClip();
        graphics.clearRect(
                orthographicWall.x() / resolution,
                orthographicWall.y() / resolution,
                orthographicWall.width() / resolution,
                orthographicWall.height() / resolution);

        graphics.setClip(
                orthographicWall.x() / resolution,
                orthographicWall.y() / resolution,
                orthographicWall.width() / resolution,
                orthographicWall.height() / resolution);

        final int maxY = orthographicWall.offset().y() / resolution + orthographicWall.height() / resolution;
        for (final var pointLight : pointLights) {
            if (pointLight.position.y() / resolution >= maxY && createLightBox(pointLight.position, pointLight.radius).intersects(orthographicWall)) {
                renderPointlight(pointLight);
            }
        }
        for (final var spotLight : spotLights) {
            if (spotLight.position.y() / resolution >= maxY && createLightBox(spotLight.position, spotLight.radius).intersects(orthographicWall)) {
                renderSpotlight(spotLight);
            }
        }
        graphics.setClip(lastClip);
    }

    private ScreenBounds createLightBox(final Offset position, final int radius) {
        return new ScreenBounds(position.x() - radius, position.y() - radius, radius * 2, radius * 2);
    }

    private void applyOpacityConfig(final Color color) {
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