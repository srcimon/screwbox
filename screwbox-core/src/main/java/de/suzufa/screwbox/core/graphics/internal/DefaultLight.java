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
import java.util.function.UnaryOperator;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener;
import de.suzufa.screwbox.core.graphics.Light;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.loop.internal.Updatable;

public class DefaultLight implements Light, Updatable, GraphicsConfigurationListener {

    private final List<Runnable> drawingTasks = new ArrayList<>();
    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Window window;
    private final LightPhysics lightPhysics = new LightPhysics();
    private final DefaultWorld world;
    private final GraphicsConfiguration configuration;

    private Lightmap lightmap;
    private Percent ambientLight = Percent.min();
    private UnaryOperator<BufferedImage> postFilter = new BlurImageFilter(3);
    private Future<Sprite> sprite = null;

    public DefaultLight(final Window window, final DefaultWorld world, final GraphicsConfiguration configuration,
            final ExecutorService executor) {
        this.executor = executor;
        this.window = window;
        this.world = world;
        this.configuration = configuration;
        configuration.registerListener(this);
        initializeLightmap();
    }

    @Override
    public Light updateShadowCasters(final List<Bounds> shadowCasters) {
        lightPhysics.setShadowCasters(shadowCasters);
        return this;
    }

    @Override
    public List<Bounds> shadowCasters() {
        return lightPhysics.shadowCasters();
    }

    @Override
    public Light addPointLight(final Vector position, final LightOptions options) {
        raiseExceptionOnSealed();
        if (!lightPhysics.isCoveredByShadowCasters(position)) {
            addPotentialGlow(position, options);
            final Bounds lightBox = Bounds.atPosition(position, options.size(), options.size());
            if (isVisible(lightBox)) {
                final List<Offset> area = new ArrayList<>();
                final List<Vector> worldArea = lightPhysics.calculateArea(lightBox);
                for (final var vector : worldArea) {
                    area.add(world.toOffset(vector));
                }
                final Offset offset = world.toOffset(position);
                drawingTasks.add(
                        () -> lightmap.addPointLight(offset, world.toDistance(options.size()), area, options.color()));
            }
        }
        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final LightOptions options) {
        raiseExceptionOnSealed();
        addPotentialGlow(position, options);
        final Bounds lightBox = Bounds.atPosition(position, options.size(), options.size());
        if (isVisible(lightBox)) {
            final Offset offset = world.toOffset(position);
            final int distance = world.toDistance(options.size());
            drawingTasks.add(() -> lightmap.addSpotLight(offset, distance, options.color()));
        }
        return this;
    }

    private void addPotentialGlow(final Vector position, final LightOptions options) {
        final double sideLength = options.size() * 3 * options.glow();
        final Bounds lightBox = Bounds.atPosition(position, sideLength, sideLength);
        if (options.glow() != 0 && isVisible(lightBox)) {
            final Color color = options.glowColor().opacity(options.glowColor().opacity().value() / 3);
            postDrawingTasks.add(() -> {
                for (int i = 1; i < 4; i++) {
                    world.drawFadingCircle(position, i * options.size() * options.glow(), color);
                }
            });
        }
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
        lightmap = new Lightmap(window.size(), configuration.lightmapResolution());
    }

    @Override
    public Light render() {
        if (isNull(sprite)) {
            throw new IllegalStateException(
                    "Light has not been sealed yet. Sealing the light AS SOON AS POSSIBLE is essential for light performance.");
        }
        try {
            window.drawSprite(sprite.get(), origin(), lightmap.resolution(), ambientLight.invert());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
        postDrawingTasks.clear();
        return this;
    }

    @Override
    public Light seal() {
        raiseExceptionOnSealed();
        sprite = executor.submit(() -> {
            for (final var drawingTask : drawingTasks) {
                drawingTask.run();
            }
            drawingTasks.clear();

            final var image = lightmap.image();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        return this;
    }

    private void raiseExceptionOnSealed() {
        if (isSealed()) {
            throw new IllegalStateException("light has already been sealed");
        }
    }

    @Override
    public Light setAmbientLight(final Percent ambientLight) {
        requireNonNull(ambientLight, "ambientLight must not be null");
        this.ambientLight = ambientLight;
        return this;
    }

    @Override
    public Percent ambientLight() {
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

    @Override
    public boolean isSealed() {
        return nonNull(sprite);
    }

}
