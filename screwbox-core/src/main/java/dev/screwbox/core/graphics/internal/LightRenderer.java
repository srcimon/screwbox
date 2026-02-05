package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Line;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import static java.util.Objects.nonNull;

class LightRenderer {

    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Viewport viewport;
    private final LightPhysics lightPhysics;
    private final UnaryOperator<BufferedImage> postFilter;
    private final boolean isLensFlareEnabled;
    private final Lightmap lightmap;

    private final List<Runnable> tasks = new ArrayList<>();

    LightRenderer(final LightPhysics lightPhysics,
                  final ExecutorService executor,
                  final Viewport viewport,
                  final boolean isLensFlareEnabled,
                  final Lightmap lightmap,
                  final UnaryOperator<BufferedImage> postFilter) {
        this.executor = executor;
        this.lightPhysics = lightPhysics;
        this.viewport = viewport;
        this.postFilter = postFilter;
        this.isLensFlareEnabled = isLensFlareEnabled;
        this.lightmap = lightmap;
    }

    public int scale() {
        return lightmap.scale();
    }

    public void addOrthographicWall(final Bounds bounds) {
        if (isVisible(bounds)) {
            final ScreenBounds screenBounds = viewport.toCanvas(bounds);
            lightmap.addOrthographicWall(screenBounds);
        }
    }

    public void addConeLight(final Vector position, final Angle direction, final Angle cone, final double radius, final Color color) {
        final double minRotation = direction.degrees() - cone.degrees() / 2.0;
        final double maxRotation = direction.degrees() + cone.degrees() / 2.0;
        addPointLight(position, radius, color, minRotation, maxRotation);
    }

    public void addPointLight(final Vector position, final double radius, final Color color) {
        addPointLight(position, radius, color, 0, 360);
    }

    private void addPointLight(final Vector position, final double radius, final Color color, final double minAngle, final double maxAngle) {
        tasks.add(() -> {
            final Bounds lightBox = createLightbox(position, radius);
            if (isVisible(lightBox) && !lightPhysics.isOccluded(position)) {
                final List<Vector> worldArea = lightPhysics.calculateArea(lightBox, minAngle, maxAngle);
                final Polygon area = mapToLightMap(worldArea);
                final Offset offset = viewport.toCanvas(position);
                final int screenRadius = viewport.toCanvas(radius);
                lightmap.addPointLight(new Lightmap.PointLight(offset, screenRadius, area, color));
            }
        });
    }

    public void addDirectionalLight(final Line source, final double distance, final Color color) {
        tasks.add(() -> {
            final var lightBox = new DirectionalLightBox(source, distance);
            if (lightBox.intersects(viewport.visibleArea()) && !lightPhysics.isOccluded(source)) {
                final List<Vector> worldArea = lightPhysics.calculateArea(lightBox);
                final Polygon area = mapToLightMap(worldArea);
                final var start = viewport.toCanvas(source.center());
                final var end = viewport.toCanvas(Angle.of(source).addDegrees(270).rotateAroundCenter(source.center(), source.center().addY(distance)));
                lightmap.addDirectionalLight(new Lightmap.DirectionalLight(start, end, area, color));
            }
        });
    }

    public void addSpotLight(final Vector position, final double radius, final Color color) {
        tasks.add(() -> {
            final Bounds lightBox = createLightbox(position, radius);
            if (isVisible(lightBox)) {
                final Offset offset = viewport.toCanvas(position);
                final int distance = viewport.toCanvas(radius);
                lightmap.addSpotlight(new Lightmap.SpotLight(offset, distance, color));
            }
        });
    }

    public Canvas canvas() {
        return viewport.canvas();
    }

    public void addGlow(final Vector position, final double radius, final Color color, final LensFlare lensFlare) {
        final Bounds lightBox = createLightbox(position, radius);
        if (isVisible(lightBox)) {
            final OvalDrawOptions options = OvalDrawOptions.fading(color);
            postDrawingTasks.add(() -> {
                if (!lightPhysics.isOccluded(position)) {
                    canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options);
                }
            });

            if (isLensFlareEnabled && nonNull(lensFlare) && viewport.visibleArea().contains(position)) {
                postDrawingTasks.add(() -> {
                    if (!lightPhysics.isOccluded(position)) {
                        lensFlare.render(position, radius, color, viewport);
                    }
                });
            }
        }
    }

    public void addGlow(final Bounds bounds, final double radius, final Color color, final LensFlare lensFlare) {
        final var lightBox = bounds.expand(radius);
        if (isVisible(lightBox)) {
            final var options = RectangleDrawOptions.fading(color).curveRadius(viewport.toCanvas(radius));
            postDrawingTasks.add(() -> canvas().drawRectangle(viewport.toCanvas(lightBox), options));

            viewport.visibleArea().intersection(bounds).ifPresent(intersection -> {
                if (isLensFlareEnabled && nonNull(lensFlare)) {
                    postDrawingTasks.add(() -> lensFlare.render(intersection, radius, color, viewport));
                }
            });
        }
    }

    public void renderGlows() {
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
    }

    public Asset<Sprite> renderLight() {
        final var asset = Asset.asset(() -> {
            for (final var task : tasks) {
                task.run();
            }
            final BufferedImage image = lightmap.createImage();
            Time time = Time.now();
            final var filtered = postFilter.apply(image);
            System.out.println(Duration.since(time).nanos());
            return Sprite.fromImage(filtered);
        });

        executor.submit(asset::load);
        return asset;
    }

    public void addAreaLight(final Bounds area, final Color color, final double curveRadius, final boolean isFadeout) {
        final Bounds lightBox = isFadeout ? area.expand(curveRadius) : area;
        if (isVisible(lightBox)) {
            final ScreenBounds bounds = viewport.toCanvas(area);
            lightmap.addAreaLight(bounds, color, viewport.toCanvas(curveRadius), isFadeout);
        }
    }

    public void addConeGlow(final Vector position, final Angle direction, final Angle cone, final double radius, final Color color) {
        final Bounds lightBox = createLightbox(position, radius);
        if (isVisible(lightBox)) {
            final double minRotation = direction.degrees() - cone.degrees() / 2.0;
            final OvalDrawOptions options = OvalDrawOptions.fading(color).startAngle(Angle.degrees(minRotation)).arcAngle(cone);
            postDrawingTasks.add(() -> {
                if (!lightPhysics.isOccluded(position)) {
                    canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options);
                }
            });
        }
    }

    private boolean isVisible(final Bounds lightBox) {
        return viewport.visibleArea().intersects(lightBox);
    }

    private Bounds createLightbox(final Vector position, final double radius) {
        return Bounds.atPosition(position, radius * 2, radius * 2);
    }

    private Polygon mapToLightMap(final List<Vector> worldArea) {
        final Polygon area = new Polygon();
        for (final var vector : worldArea) {
            final var offset = viewport.toCanvas(vector);
            area.addPoint(offset.x() / lightmap.scale(), offset.y() / lightmap.scale());
        }
        return area;
    }
}
