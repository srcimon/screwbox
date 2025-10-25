package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
            if (isVisible(lightBox)) {
                final List<Offset> area = new ArrayList<>();
                final List<Vector> worldArea = lightPhysics.calculateArea(lightBox, minAngle, maxAngle);
                for (final var vector : worldArea) {
                    area.add(viewport.toCanvas(vector));
                }
                final Offset offset = viewport.toCanvas(position);
                final int screenRadius = viewport.toCanvas(radius);
                lightmap.addPointLight(new Lightmap.PointLight(offset, screenRadius, area, color));
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
            postDrawingTasks.add(() -> canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options));

            if (isLensFlareEnabled && nonNull(lensFlare) && viewport.visibleArea().contains(position)) {
                postDrawingTasks.add(() -> lensFlare.render(position, radius, color, viewport));
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
        final var asyncTasks = new ArrayList<>(tasks);
        for (final var task : asyncTasks) {
            task.run();
        }
        final var spriteFuture = executor.submit(() -> {

            final BufferedImage image = lightmap.createImage();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        return Asset.asset(() -> {
            try {
                return spriteFuture.get();
            } catch (final InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error receiving lightmap sprite", e);
            }
        });
    }

    private boolean isVisible(final Bounds lightBox) {
        return canvas().isVisible(viewport.toCanvas(lightBox));
    }

    private Bounds createLightbox(final Vector position, final double radius) {
        return Bounds.atPosition(position, radius * 2, radius * 2);
    }

    public void addAreaLight(final Bounds area, final Color color, final double curveRadius, final boolean isFadeout) {
        final Bounds lightBox = isFadeout ? area.expand(curveRadius) : area;
        if (isVisible(lightBox)) {
            final ScreenBounds bounds = viewport.toCanvas(area);
            lightmap.addAreaLight(bounds, color, viewport.toCanvas(curveRadius), isFadeout);
        }
    }

}
