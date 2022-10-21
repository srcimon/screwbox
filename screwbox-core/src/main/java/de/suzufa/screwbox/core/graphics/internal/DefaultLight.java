package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty.LIGHTMAP_BLUR;
import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener;
import de.suzufa.screwbox.core.graphics.Light;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.loop.internal.Updatable;

public class DefaultLight implements Light, Updatable, GraphicsConfigurationListener {

    private final List<Runnable> drawingTasks = new ArrayList<>();
    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Window window;
    private final LightPhysics lightPhysics;
    private final DefaultWorld world;
    private final GraphicsConfiguration configuration;

    private Lightmap lightmap;
    private Percentage ambientLight = Percentage.min();
    private Function<BufferedImage, BufferedImage> postFilter = new BlurImageFilter(3);
    private Future<Sprite> sprite = null;

    public DefaultLight(final Window window, final DefaultWorld world, final GraphicsConfiguration configuration,
            final ExecutorService executor) {
        this.executor = executor;
        this.window = window;
        this.world = world;
        this.configuration = configuration;
        this.lightPhysics = new LightPhysics(world);
        configuration.registerListener(this);
        initializeLightmap();
    }

    @Override
    public Light updateObstacles(final List<Bounds> obstacles) {
        lightPhysics.setObstacles(obstacles);
        return this;
    }

    @Override
    public List<Bounds> obstacles() {
        return lightPhysics.obstacles();
    }

    @Override
    public Light addPointLight(final Vector position, final double range, final Color color) {
        // TODO: error after sealed
        final Bounds lightBox = Bounds.atPosition(position, range, range);
        if (isVisible(lightBox)) {
            drawingTasks.add(() -> {
                final List<Offset> area = lightPhysics.calculateArea(lightBox);
                lightmap.addPointLight(world.toOffset(position), world.toDistance(range), area, color);
            });

        }
        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final double range, final Color color) {
        // TODO: error after sealed
        final Bounds lightBox = Bounds.atPosition(position, range, range);
        if (isVisible(lightBox)) {
            drawingTasks.add(() -> lightmap.addSpotLight(world.toOffset(position), world.toDistance(range), color));

        }
        return this;
    }

    @Override
    public Light addLensFlare(Vector origin, double size) {
        // TODO: error after sealed
        Offset offset = world.toOffset(origin);
        if (window.isVisible(offset)) {
            postDrawingTasks.add(() -> lightmap.addLensFlare(offset, window.center(), world.toDistance(size)));
        }
        return this;
    }

    @Override
    public void update() {
        initializeLightmap();
        sprite = null;
    }

    private void initializeLightmap() {
        if (nonNull(lightmap)) {
            lightmap.close();
        }
        lightmap = new Lightmap(window.size(), configuration.lightmapResolution(), configuration.isUseAntialising());
    }

    @Override
    public Light drawLightmap() {
        if (isNull(sprite)) {
            throw new IllegalStateException(
                    "Light has not been sealed yet. Sealing the light AS SOON AS POSSIBLE is essential for light performance.");
        }
        try {
            window.drawSprite(sprite.get(), origin(), lightmap.resolution(), ambientLight.invert());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    @Override
    public Light seal() {
        if (nonNull(sprite)) {
            throw new IllegalStateException("lightmap has already been sealed");
        }
        sprite = executor.submit(() -> {
            for (final var drawingTask : drawingTasks) {
                drawingTask.run();
            }
            drawingTasks.clear();

            lightmap.invertImage();

            for (final var drawingTask : postDrawingTasks) {
                drawingTask.run();
            }
            postDrawingTasks.clear();

            final var image = lightmap.image();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        return this;
    }

    @Override
    public Light setAmbientLight(final Percentage ambientLight) {
        requireNonNull(ambientLight, "ambientLight must not be null");
        this.ambientLight = ambientLight;
        return this;
    }

    @Override
    public Percentage ambientLight() {
        return ambientLight;
    }

    @Override
    public void configurationChanged(final ConfigurationProperty changedProperty) {
        if (LIGHTMAP_BLUR.equals(changedProperty)) {
            postFilter = configuration.lightmapBlur() == 0
                    ? doNothing -> doNothing
                    : new BlurImageFilter(configuration.lightmapBlur());
        }
    }

    private boolean isVisible(final Bounds lightBox) {
        return window.isVisible(world.toWindowBounds(lightBox));
    }

}
