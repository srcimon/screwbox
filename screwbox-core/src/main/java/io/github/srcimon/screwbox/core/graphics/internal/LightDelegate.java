package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
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

import static io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions.scaled;

public class LightDelegate {

    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final GraphicsConfiguration configuration;
    private final Viewport viewport;
    private final LightPhysics lightPhysics;
    private final UnaryOperator<BufferedImage> postFilter;
    private Lightmap lightmap;
    private Percent ambientLight = Percent.zero();

    private boolean renderInProgress = false;

    private final List<Runnable> tasks = new ArrayList<>();

    public LightDelegate(final LightPhysics lightPhysics,
                         final GraphicsConfiguration configuration,
                         final ExecutorService executor, final Viewport viewport,
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
            final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
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
            final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
            if (isVisible(lightBox)) {
                final Offset offset = viewport.toCanvas(position);
                final int distance = viewport.toCanvas(radius);
                lightmap.add(new Lightmap.SpotLight(offset, distance, color));
            }
        });
    }

    public void addGlow(final Vector position, final double radius, final Color color) {
        final CircleDrawOptions options = CircleDrawOptions.fading(color);
        final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
        if (isVisible(lightBox)) {
            postDrawingTasks.add(() -> viewport.canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options));
        }
    }

    public void render() {
        if (renderInProgress) {
            throw new IllegalStateException("rendering lights is already in progress");
        }

        renderInProgress = true;
        if (!ambientLight.isMax()) {
            renderLightmap();//TODO remove ambient light from light delegate!
        }
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
    }

    private void renderLightmap() {
        for (final var task : tasks) {
            task.run();
        }
        final var spriteFuture = executor.submit(() -> {
            final BufferedImage image = lightmap.createImage();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        final Asset<Sprite> sprite = Asset.asset(() -> {
            try {
                return spriteFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error receiving lightmap sprite", e);
            }
        });
        // Avoid flickering by overdraw at last by one pixel
        final var overlap = Math.max(1, configuration.lightmapBlur()) * -configuration.lightmapScale();
        viewport.canvas().drawSprite(sprite, Offset.at(overlap, overlap), scaled(configuration.lightmapScale()).opacity(ambientLight.invert()));
    }

    public void setAmbientLight(final Percent ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void update() {
        renderInProgress = false;
        initLightmap();
        tasks.clear();
        postDrawingTasks.clear();
    }

    private boolean isVisible(final Bounds lightBox) {
        return viewport.canvas().isVisible(viewport.toCanvas(lightBox));//TODO this can easily be inlined
    }

    private void initLightmap() {
        lightmap = new Lightmap(viewport.canvas().size(), configuration.lightmapScale(), configuration.lightFalloff());
    }

}
