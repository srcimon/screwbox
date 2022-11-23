package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty.LIGHTMAP_BLUR;
import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener;
import de.suzufa.screwbox.core.graphics.Light;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.internal.Lightmap.PointLight;
import de.suzufa.screwbox.core.graphics.internal.Lightmap.SpotLight;
import de.suzufa.screwbox.core.loop.internal.Updatable;

public class DefaultLight implements Light, Updatable, GraphicsConfigurationListener {

    private final List<Runnable> postDrawingTasks = new ArrayList<>();
    private final ExecutorService executor;
    private final Screen screen;
    private final LightPhysics lightPhysics = new LightPhysics();
    private final DefaultWorld world;
    private final GraphicsConfiguration configuration;
    private Lightmap lightmap;
    private Percent ambientLight = Percent.min();
    private UnaryOperator<BufferedImage> postFilter = new BlurImageFilter(3);

    private List<Runnable> tasks = new ArrayList<>();

    public DefaultLight(final Screen screen, final DefaultWorld world, final GraphicsConfiguration configuration,
            final ExecutorService executor) {
        this.executor = executor;
        this.screen = screen;
        this.world = world;
        this.configuration = configuration;
        configuration.registerListener(this);
        initLightmap();
    }

    @Override
    public Light addShadowCasters(final List<Bounds> shadowCasters) {
        lightPhysics.addShadowCasters(shadowCasters);
        return this;
    }

    @Override
    public List<Bounds> shadowCasters() {
        return lightPhysics.shadowCasters();
    }

    @Override
    public Light addFullBrightnessArea(final Bounds area) {
        if (isVisible(area)) {
            final WindowBounds bounds = world.toWindowBounds(area);
            lightmap.add(bounds);
        }
        return this;
    }

    @Override
    public Light addPointLight(final Vector position, final LightOptions options) {
        tasks.add(() -> {
            if (!lightPhysics.isCoveredByShadowCasters(position)) {
                addPotentialGlow(position, options);
                final Bounds lightBox = Bounds.atPosition(position, options.radius() * 2, options.radius() * 2);
                if (isVisible(lightBox)) {
                    final List<Offset> area = new ArrayList<>();
                    final List<Vector> worldArea = lightPhysics.calculateArea(lightBox);
                    for (final var vector : worldArea) {
                        area.add(world.toOffset(vector));
                    }
                    final Offset offset = world.toOffset(position);
                    final int screenRadius = world.toDistance(options.radius());
                    lightmap.add(new PointLight(offset, screenRadius, area, options.color()));
                }
            }
        });

        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final LightOptions options) {
        tasks.add(() -> {
            addPotentialGlow(position, options);
            final Bounds lightBox = Bounds.atPosition(position, options.radius() * 2, options.radius() * 2);
            if (isVisible(lightBox)) {
                final Offset offset = world.toOffset(position);
                final int distance = world.toDistance(options.radius());
                lightmap.add(new SpotLight(offset, distance, options.color()));
            }
        });
        return this;
    }

    private void addPotentialGlow(final Vector position, final LightOptions options) {
        final double sideLength = options.radius() * 3 * options.glow();
        final Bounds lightBox = Bounds.atPosition(position, sideLength, sideLength);
        if (options.glow() != 0 && isVisible(lightBox)) {
            final Color color = options.glowColor().opacity(options.glowColor().opacity().value() / 3);
            postDrawingTasks.add(() -> {
                for (int i = 1; i < 4; i++) {
                    world.drawFadingCircle(position, i * options.radius() * options.glow(), color);
                }
            });
        }
    }

    @Override
    public Light render() {
        final var copiedLightmap = lightmap;
        for (var task : tasks) {
            task.run();
        }
        final var spriteFuture = executor.submit(() -> {
            final BufferedImage image = copiedLightmap.createImage();
            final var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        Asset<Sprite> sprite = Asset.asset(() -> {
            try {
                return spriteFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error receiving lightmap sprite");
            }
        });

        screen.drawSprite(sprite, origin(), configuration.lightmapResolution(), ambientLight.invert());
        for (final var drawingTask : postDrawingTasks) {
            drawingTask.run();
        }
        return this;
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
        return screen.isVisible(world.toWindowBounds(lightBox));
    }

    @Override
    public void update() {
        initLightmap();
        tasks.clear();
        postDrawingTasks.clear();
        lightPhysics.shadowCasters().clear();
    }

    private void initLightmap() {
        lightmap = new Lightmap(screen.size(), configuration.lightmapResolution());
    }

}
