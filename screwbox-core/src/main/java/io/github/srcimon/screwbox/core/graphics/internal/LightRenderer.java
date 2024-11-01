package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class LightRenderer {

    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final GraphicsConfiguration configuration;
    private final Viewport viewport;
    private final LightPhysics lightPhysics;
    private final UnaryOperator<BufferedImage> postFilter;
    private Lightmap lightmap;

    private final List<Runnable> tasks = new ArrayList<>();

    public LightRenderer(final LightPhysics lightPhysics,
                         final GraphicsConfiguration configuration,
                         final ExecutorService executor,
                         final Viewport viewport,
                         final UnaryOperator<BufferedImage> postFilter) {
        this.executor = executor;
        this.lightPhysics = lightPhysics;
        this.configuration = configuration;
        this.viewport = viewport;
        this.postFilter = postFilter;
        initLightmap();
    }

    public void addFullBrightnessArea(final Bounds area) {
        if (isVisible(area)) {
            final ScreenBounds bounds = viewport.toCanvas(area);
            lightmap.add(bounds);
        }
    }

    public void addConeLight(final Vector position, final Rotation direction, final Rotation cone, final double radius, final Color color) {
        double minRotation = direction.degrees() - cone.degrees() / 2.0;
        double maxRotation = direction.degrees() + cone.degrees() / 2.0;
        addPointLight(position, radius, color, minRotation, maxRotation);
    }

    public void addPointLight(final Vector position, final double radius, final Color color) {
        addPointLight(position, radius, color, 0, 360);
    }

    private void addPointLight(final Vector position, final double radius, final Color color, double minAngle, double maxAngle) {
        tasks.add(() -> {
            final Bounds lightBox = lightLightBox(position, radius);
            if (isVisible(lightBox)) {
                final List<Offset> area = new ArrayList<>();
                final List<Vector> worldArea = lightPhysics.calculateArea(lightBox, minAngle, maxAngle);
                for (final var vector : worldArea) {
                    area.add(viewport.toCanvas(vector));
                }
                final Offset offset = viewport.toCanvas(position);
                final int screenRadius = viewport.toCanvas(radius);
                lightmap.add(new Lightmap.PointLight(offset, screenRadius, area, color));
            }
        });
    }

    public void addSpotLight(final Vector position, final double radius, final Color color) {
        tasks.add(() -> {
            final Bounds lightBox = lightLightBox(position, radius);
            if (isVisible(lightBox)) {
                final Offset offset = viewport.toCanvas(position);
                final int distance = viewport.toCanvas(radius);
                lightmap.add(new Lightmap.SpotLight(offset, distance, color));
            }
        });
    }

    public Canvas canvas() {
        return viewport.canvas();
    }

    public void addGlow(final Vector position, final double radius, final Color color) {
        final Bounds lightBox = lightLightBox(position, radius);
        if (isVisible(lightBox)) {
            final CircleDrawOptions options = CircleDrawOptions.fading(color);
            postDrawingTasks.add(() -> viewport.canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options));
        }
    }

    public void renderGlows() {
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
    }

    public Asset<Sprite> renderLight() {
        for (final var task : tasks) {
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
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error receiving lightmap sprite", e);
            }
        });
    }

    private boolean isVisible(final Bounds lightBox) {
        return viewport.canvas().isVisible(viewport.toCanvas(lightBox));
    }

    private void initLightmap() {
        lightmap = new Lightmap(canvas().size(), configuration.lightmapScale(), configuration.lightFalloff());
    }

    private Bounds lightLightBox(final Vector position, final double radius) {
        return Bounds.atPosition(position, radius * 2, radius * 2);
    }
}
