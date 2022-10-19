package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.graphics.Offset.origin;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.internal.DefaultWorld;
import de.suzufa.screwbox.core.graphics.light.Light;
import de.suzufa.screwbox.core.loop.internal.Updatable;

public class DefaultLight implements Light, Updatable {

    private final ExecutorService executor; // TODO: use for async preperation
    private final Window window;
    private final LightPhysics lightPhysics;

    private Percentage ambientLight = Percentage.min();
    private int resolution = 4;
    private boolean isUseAntialiasing = true;
    private Lightmap lightmap;
    private DefaultWorld world;
    private Function<BufferedImage, BufferedImage> postFilter = new BlurImageFilter(3);
    private List<Future<Runnable>> drawingTasks = new ArrayList<>();
    Future<Sprite> sprite = null;

    public DefaultLight(final Window window, final DefaultWorld world, final ExecutorService executor) {
        this.executor = executor;
        this.window = window;
        this.world = world;
        this.lightPhysics = new LightPhysics(world);
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
        Bounds lightBox = Bounds.atPosition(position, range, range);
        if (isVisible(lightBox)) {
            drawingTasks.add(executor.submit(new Callable<Runnable>() {

                @Override
                public Runnable call() throws Exception {
                    List<Offset> area = lightPhysics.calculateArea(lightBox);
                    return new Runnable() {

                        @Override
                        public void run() {
                            lightmap.addPointLight(world.toOffset(position), world.toDistance(range), area, color);
//                            lightmap.addSpotLight(world.toOffset(position), world.toDistance(range), color);
                        }

                    };
                }
            }));

        }
        return this;
    }

    private boolean isVisible(Bounds lightBox) {
        return window.isVisible(world.toWindowBounds(lightBox));
    }

    @Override
    public void update() {
        initializeLightmap();
    }

    @Override
    public Light setResolution(final int resolution) {
        // TODO: validation
        this.resolution = resolution;
        return this;
    }

    @Override
    public int resolution() {
        return resolution;
    }

    private void initializeLightmap() {
        if (lightmap != null) {
            lightmap.close();
        }
        lightmap = new Lightmap(window.size(), resolution, isUseAntialiasing);
    }

    @Override
    public Light setUseAntialiasing(final boolean useAntialiasing) {
        // TODO: validation
        this.isUseAntialiasing = useAntialiasing;
        return this;
    }

    @Override
    public boolean isUseAntialiasing() {
        return isUseAntialiasing;
    }

    @Override
    public Light drawLightmap() {
        if (sprite == null) {
            throw new IllegalStateException("lightmap has not been sealed yet");
        }
        // TODO: if not seal create but log warning on first time
        try {
            window.drawSprite(sprite.get(), origin(), lightmap.resolution(), ambientLight.invert(), Angle.none());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("error receiving lightmap sprite", e);
        }
        return this;
    }

    @Override
    public Light seal() {
        sprite = executor.submit(() -> {
            for (var drawingTask : drawingTasks) {
                drawingTask.get().run();
            }
            drawingTasks.clear();
            var image = lightmap.createSprite();
            var filtered = postFilter.apply(image);
            return Sprite.fromImage(filtered);
        });
        return this;
    }

    @Override
    public Light setAmbientLight(Percentage ambientLight) {
        // TODO: non null
        this.ambientLight = ambientLight;
        return this;
    }

    @Override
    public Percentage ambientLight() {
        return ambientLight;
    }

    @Override
    public Light setBlur(int blur) {
        if (blur == 0) {
            this.postFilter = doNothing -> doNothing;
        } else {
            this.postFilter = new BlurImageFilter(blur);
        }
        return this;
    }

}
