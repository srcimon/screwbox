package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent;
import io.github.srcimon.screwbox.core.graphics.Light;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public class DefaultLight implements Light {

    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Screen screen;
    private final LightPhysics lightPhysics = new LightPhysics();
    private final DefaultWorld world;
    private final GraphicsConfiguration configuration;
    private Lightmap lightmap;
    private Percent ambientLight = Percent.zero();
    private UnaryOperator<BufferedImage> postFilter = new BlurImageFilter(3);
    private boolean renderInProgress = false;

    private final List<Runnable> tasks = new ArrayList<>();

    public DefaultLight(final Screen screen, final DefaultWorld world, final GraphicsConfiguration configuration,
                        final ExecutorService executor) {
        this.executor = executor;
        this.screen = screen;
        this.world = world;
        this.configuration = configuration;
        configuration.addListener(event -> {
            if (GraphicsConfigurationEvent.ConfigurationProperty.LIGHTMAP_BLUR.equals(event.changedProperty())) {
                postFilter = configuration.lightmapBlur() == 0
                        ? doNothing -> doNothing
                        : new BlurImageFilter(configuration.lightmapBlur());
            }
        });
        initLightmap();
    }

    @Override
    public Light addShadowCasters(final List<Bounds> shadowCasters) {
        lightPhysics.addShadowCasters(shadowCasters);
        return this;
    }

    @Override
    public Light addShadowCaster(final Bounds shadowCaster) {
        lightPhysics.addShadowCaster(shadowCaster);
        return this;
    }

    @Override
    public Light addFullBrightnessArea(final Bounds area) {
        if (isVisible(area)) {
            final ScreenBounds bounds = world.toScreen(area);
            lightmap.add(bounds);
        }
        return this;
    }

    @Override
    public Light addConeLight(final Vector position, final Rotation direction, final Rotation cone, final double radius, final Color color) {
        double minRotation = direction.degrees() - cone.degrees() / 2.0;
        double maxRotation = direction.degrees() + cone.degrees() / 2.0;
        addPointLight(position, radius, color, minRotation, maxRotation);

        return this;
    }

    @Override
    public Light addPointLight(final Vector position, final double radius, final Color color) {
        addPointLight(position, radius, color, 0, 360);

        return this;
    }

    private void addPointLight(final Vector position, final double radius, final Color color, double minAngle, double maxAngle) {
        tasks.add(() -> {
            if (!lightPhysics.isCoveredByShadowCasters(position)) {
                final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
                if (isVisible(lightBox)) {
                    final List<Offset> area = new ArrayList<>();
                    final List<Vector> worldArea = lightPhysics.calculateArea(lightBox, minAngle, maxAngle);
                    for (final var vector : worldArea) {
                        area.add(world.toOffset(vector));
                    }
                    final Offset offset = world.toOffset(position);
                    final int screenRadius = world.toDistance(radius);
                    lightmap.add(new Lightmap.PointLight(offset, screenRadius, area, color));
                }
            }
        });
    }

    @Override
    public Light addSpotLight(final Vector position, final double radius, final Color color) {
        tasks.add(() -> {
            final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
            if (isVisible(lightBox)) {
                final Offset offset = world.toOffset(position);
                final int distance = world.toDistance(radius);
                lightmap.add(new Lightmap.SpotLight(offset, distance, color));
            }
        });
        return this;
    }

    @Override
    public Light addGlow(final Vector position, final double radius, final Color color) {
        final Bounds lightBox = Bounds.atPosition(position, radius * 2, radius * 2);
        if (radius != 0 && isVisible(lightBox)) {
            postDrawingTasks.add(() -> {
                for (int i = 1; i < 4; i++) {
                    world.drawFadingCircle(position, i * radius, color);
                }
            });
        }
        return this;
    }

    @Override
    public Light render() {
        if (renderInProgress) {
            throw new IllegalStateException("rendering lights is already in progress");
        }

        renderInProgress = true;
        final var copiedLightmap = lightmap;
        for (final var task : tasks) {
            task.run();
        }
        final var spriteFuture = executor.submit(() -> {
            final BufferedImage image = copiedLightmap.createImage();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        final Asset<Sprite> sprite = Asset.asset(() -> {
            try {
                return spriteFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error receiving lightmap sprite");
            }
        });

        screen.drawSprite(sprite, Offset.origin(), configuration.lightmapScale(), ambientLight.invert());
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
        return this;
    }

    @Override
    public Light setAmbientLight(final Percent ambientLight) {
        requireNonNull(ambientLight, "ambient light must not be null");
        this.ambientLight = ambientLight;
        return this;
    }

    @Override
    public Percent ambientLight() {
        return ambientLight;
    }

    public void update() {
        renderInProgress = false;
        initLightmap();
        tasks.clear();
        postDrawingTasks.clear();
        lightPhysics.shadowCasters().clear();
    }

    private boolean isVisible(final Bounds lightBox) {
        return screen.isVisible(world.toScreen(lightBox));
    }

    private void initLightmap() {
        lightmap = new Lightmap(screen.size(), configuration.lightmapScale(), configuration.lightFalloff());
    }
}
