package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.AlphaComposite.SRC_OVER;

class Lightmap {

    record AreaLight(ScreenBounds bounds, Color color, double curveRadius, boolean isFadeout) {
    }

    record PointLight(Offset position, int radius, Polygon area, Color color) {
    }

    record SpotLight(Offset position, int radius, Color color) {
    }

    public record DirectionalLight(Offset start, Offset end, Polygon area, Color color) {
    }

    public record BackdropOccluder(Polygon area, double distance) {

    }

    private static final java.awt.Color FADE_TO_COLOR = AwtMapper.toAwtColor(Color.TRANSPARENT);
    private final Frame map;
    private final Graphics2D graphics;
    private final int scale;
    private final float[] fractions;
    private final Size lightMapSize;
    private final Canvas lightCanvas;
    private final List<PointLight> pointLights = new ArrayList<>();
    private final List<SpotLight> spotLights = new ArrayList<>();
    private final List<AreaLight> areaLights = new ArrayList<>();
    private final List<DirectionalLight> directionalLights = new ArrayList<>();
    private final List<ScreenBounds> orthographicWalls = new ArrayList<>();
    private final List<BackdropOccluder> backdropOccluders = new ArrayList<>();

    public Lightmap(final Size size, final int scale, final Percent lightFalloff) {
        lightMapSize = Size.of(
            Math.max(1, size.width() / scale),
            Math.max(1, size.height() / scale));
        this.map = Frame.empty(lightMapSize);
        this.scale = scale;
        this.graphics = map.image().createGraphics();
        this.graphics.setBackground(AwtMapper.toAwtColor(Color.TRANSPARENT));
        final double value = lightFalloff.invert().value();
        final float falloffValue = (float) Math.clamp(value, 0.1f, 0.99f);
        this.fractions = new float[]{falloffValue, 1f};
        this.lightCanvas = map.canvas();
    }

    public void addBackdropOccluder(BackdropOccluder backdropOccluder) {
        backdropOccluders.add(backdropOccluder);
    }

    public void addDirectionalLight(final DirectionalLight directionalLight) {
        directionalLights.add(directionalLight);
    }


    public void addOrthographicWall(final ScreenBounds screenBounds) {
        orthographicWalls.add(screenBounds);
    }

    public void addAreaLight(final ScreenBounds bounds, final Color color, final double curveRadius, final boolean isFadeout) {
        areaLights.add(new AreaLight(bounds, color, curveRadius, isFadeout));
    }

    public void addPointLight(final PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public void addSpotlight(final SpotLight spotLight) {
        spotLights.add(spotLight);
    }

    public int scale() {
        return scale;
    }

    public BufferedImage createImage() {
        //TODO implement backdropOccluders within all relevant light sources
        for (final var pointLight : pointLights) {
            renderPointLight(pointLight);
        }
        for (final var spotLight : spotLights) {
            renderSpotlight(spotLight);
        }
        for (final var directionalLight : directionalLights) {
            renderDirectionalLight(directionalLight);
        }
        for (final var orthographicWall : orthographicWalls) {
            renderOrthographicWall(orthographicWall);
        }
        for (final var areaLight : areaLights) {
            renderAreaLight(areaLight);
        }
        graphics.dispose();
        ImageOperations.invertOpacity(map.image());
        return map.image();
    }

    private void renderDirectionalLight(final DirectionalLight directionalLight) {
        applyOpacityConfig(directionalLight.color());
        Offset start = directionalLight.start();
        Offset end = directionalLight.end();
        graphics.setPaint(new GradientPaint(
            (float) start.x() / scale, (float) start.y() / scale,
            AwtMapper.toAwtColor(directionalLight.color().opacity(1)),
            (float) end.x() / scale, (float) end.y() / scale,
            FADE_TO_COLOR));

        graphics.fillPolygon(directionalLight.area);
    }

    private void renderPointLight(final PointLight pointLight) {
        var clipArea = new Area(new Rectangle2D.Double(0,0, lightMapSize.width(), lightMapSize.height()));
        //TODO only when intersects
        for(final var occluder : backdropOccluders) {//TODO directly store areas?

            Polygon translatedPolygon = translateRelativeToLightSource(occluder, pointLight.position);
            List<Offset> translatedOffsets = toOffsets(translatedPolygon);
            var translatedSmoothed = createPolygonPath(translatedOffsets);
            clipArea.subtract(new Area(translatedSmoothed));
        }
        for(final var occluder : backdropOccluders) {//TODO directly store areas?
             var smoothed = createPolygonPath(toOffsets(occluder.area));
            clipArea.add(new Area(smoothed));
        }
        graphics.setClip(clipArea);
        final var paint = radialPaint(pointLight.position(), pointLight.radius(), pointLight.color());
        applyOpacityConfig(pointLight.color());
        graphics.setPaint(paint);
        graphics.fillPolygon(pointLight.area);
        graphics.setClip(null);
    }

    private static List<Offset> toOffsets(Polygon translatedPolygon) {
        List<Offset> translatedOffsets = new ArrayList<>();
        for(int i = 0; i < translatedPolygon.npoints; i++) {
            translatedOffsets.add(Offset.at(translatedPolygon.xpoints[i], translatedPolygon.ypoints[i]));
        }
        return translatedOffsets;
    }

    //TODO move duplication outside!
    private static GeneralPath createPolygonPath(final List<Offset> nodes) {
        final var path = new GeneralPath();
        final Offset firstNode = nodes.getFirst();
        final boolean isCircular = nodes.getFirst().equals(nodes.getLast());
        path.moveTo(firstNode.x(), firstNode.y());
        for (int nodeNr = 0; nodeNr < nodes.size(); nodeNr++) {
                addSplinePathNode(nodes, nodeNr, isCircular, path);
        }
        return path;
    }

    private static void addSplinePathNode(final List<Offset> nodes, final int nodeNr, final boolean isCircular, final GeneralPath path) {
        if (nodeNr < nodes.size() - 1) {
            if (isCircular) {
                addCircularSplinePathNode(nodes, nodeNr, path);
            } else {
                addSplinePathNode(nodes, nodeNr, path);
            }
        }
    }

    private static void addSplinePathNode(final List<Offset> nodes, final int nodeNr, final GeneralPath path) {
        final Offset currentNode = nodes.get(nodeNr);
        final Offset nextNode = nodes.get((nodeNr + 1) % nodes.size());
        final Offset previous = nodes.get((nodeNr - 1 + nodes.size()) % nodes.size());
        final Offset next = nodes.get((nodeNr + 2) % nodes.size());

        final boolean isEnd = nodeNr >= nodes.size() - 2;
        final double leftX = nodeNr == 0 ? currentNode.x() : currentNode.x() + (nextNode.x() - previous.x()) / MAGIC_SPLINE_NUMBER;
        final double leftY = nodeNr == 0 ? currentNode.y() : currentNode.y() + (nextNode.y() - previous.y()) / MAGIC_SPLINE_NUMBER;
        final double rightX = isEnd ? nextNode.x() : nextNode.x() - (next.x() - currentNode.x()) / MAGIC_SPLINE_NUMBER;
        final double rightY = isEnd ? nextNode.y() : nextNode.y() - (next.y() - currentNode.y()) / MAGIC_SPLINE_NUMBER;

        path.curveTo(
            leftX, leftY, // bezier 1
            rightX, rightY,  // bezier 2
            nextNode.x(), nextNode.y()); // destination
    }

    private static void addCircularSplinePathNode(final List<Offset> nodes, final int nodeNr, final GeneralPath path) {
        final Offset currentNode = nodes.get(nodeNr);
        final Offset nextNode = nodes.get((nodeNr + 1) % nodes.size());
        final Offset previous = nodes.get((nodeNr - 1 + nodes.size() - 1) % (nodes.size() - 1));
        final Offset next = nodes.get((nodeNr + 2) % (nodes.size() - 1));
        final double leftX = currentNode.x() + (nextNode.x() - previous.x()) / MAGIC_SPLINE_NUMBER;
        final double leftY = currentNode.y() + (nextNode.y() - previous.y()) / MAGIC_SPLINE_NUMBER;
        final double rightX = nextNode.x() - (next.x() - currentNode.x()) / MAGIC_SPLINE_NUMBER;
        final double rightY = nextNode.y() - (next.y() - currentNode.y()) / MAGIC_SPLINE_NUMBER;

        path.curveTo(
            leftX, leftY, // bezier 1
            rightX, rightY,  // bezier 2
            nextNode.x(), nextNode.y()); // destination
    }

    private static final double MAGIC_SPLINE_NUMBER = 6.0;

    private Polygon translateRelativeToLightSource(final BackdropOccluder occluder, final Offset position) {
        int[] translatedX = new int[occluder.area.npoints];
        int[] translatedY = new int[occluder.area.npoints];

        for (int i = 0; i < occluder.area.npoints; i++) {

            int xDist = position.x() / scale - occluder.area.xpoints[i];
            int yDist = position.y() / scale - occluder.area.ypoints[i];

            translatedX[i] = occluder.area.xpoints[i] + (int)(xDist * -occluder.distance) ;
            translatedY[i] = occluder.area.ypoints[i] + (int)(yDist *- occluder.distance);
        }

        return new Polygon(translatedX, translatedY, occluder.area.npoints);
    }

    private void renderSpotlight(final SpotLight spotLight) {
        final var paint = radialPaint(spotLight.position(), spotLight.radius(), spotLight.color());
        graphics.setPaint(paint);
        applyOpacityConfig(spotLight.color());
        graphics.fillOval(
            spotLight.position().x() / scale - spotLight.radius() / scale,
            spotLight.position().y() / scale - spotLight.radius() / scale,
            spotLight.radius() / scale * 2,
            spotLight.radius() / scale * 2);
    }

    private void renderAreaLight(final AreaLight light) {
        final int curveRadius = (int) (light.curveRadius / scale);

        final var screenBounds = light.isFadeout
            ? new ScreenBounds(
            (int) ((light.bounds.offset().x() - light.curveRadius) / scale),
            (int) ((light.bounds.offset().y() - light.curveRadius) / scale),
            (int) ((light.bounds.width() + 2.0 * light.curveRadius) / scale),
            (int) ((light.bounds.height() + 2.0 * light.curveRadius) / scale))
            : new ScreenBounds(
            light.bounds.offset().x() / scale,
            light.bounds.offset().y() / scale,
            light.bounds.width() / scale,
            light.bounds.height() / scale);

        lightCanvas.drawRectangle(screenBounds, light.isFadeout
            ? RectangleDrawOptions.fading(light.color).curveRadius(curveRadius)
            : RectangleDrawOptions.filled(light.color).curveRadius(curveRadius));
    }

    private void renderOrthographicWall(final ScreenBounds orthographicWall) {
        final var lastClip = graphics.getClip();
        graphics.clearRect(
            orthographicWall.x() / scale,
            orthographicWall.y() / scale,
            orthographicWall.width() / scale,
            orthographicWall.height() / scale);

        graphics.setClip(
            orthographicWall.x() / scale,
            orthographicWall.y() / scale,
            orthographicWall.width() / scale,
            orthographicWall.height() / scale);

        final int maxY = orthographicWall.offset().y() / scale + orthographicWall.height() / scale;
        for (final var pointLight : pointLights) {
            if (pointLight.position.y() / scale >= maxY && createLightBox(pointLight.position, pointLight.radius).intersects(orthographicWall)) {
                renderPointLight(pointLight);
            }
        }
        for (final var spotLight : spotLights) {
            if (spotLight.position.y() / scale >= maxY && createLightBox(spotLight.position, spotLight.radius).intersects(orthographicWall)) {
                renderSpotlight(spotLight);
            }
        }
        graphics.setClip(lastClip);
    }

    private static ScreenBounds createLightBox(final Offset position, final int radius) {
        return new ScreenBounds(position.x() - radius, position.y() - radius, radius * 2, radius * 2);
    }

    private void applyOpacityConfig(final Color color) {
        graphics.setComposite(AlphaComposite.getInstance(SRC_OVER, (float) color.opacity().value()));
    }

    private RadialGradientPaint radialPaint(final Offset position, final int radius, final Color color) {
        final var usedRadius = Math.max(radius, scale);
        final var colors = new java.awt.Color[]{AwtMapper.toAwtColor(color.opacity(1)), FADE_TO_COLOR};

        return new RadialGradientPaint(
            position.x() / (float) scale,
            position.y() / (float) scale,
            usedRadius / (float) scale,
            fractions, colors);
    }

}