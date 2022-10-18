package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.graphics.Offset.origin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
    private LightmapNewAndCool lightmap;
    private DefaultWorld world;

    private List<Runnable> drawingTasks = new ArrayList<>();
    Future<?> currentTaskWorker = null;

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
            drawingTasks.add(new Runnable() {

                @Override
                public void run() {
                    List<Offset> area = lightPhysics.calculateArea(lightBox);
                    lightmap.addPointLight(world.toOffset(position), world.toDistance(range), area, color);
//                    lightmap.addSpotLight(world.toOffset(position), world.toDistance(range), color);
                }

            });
        }
        // TODO: invocations can get Lost here!!!!
        if (currentTaskWorker == null) {

            List<Runnable> tasks = new ArrayList<>(drawingTasks);
            drawingTasks.clear();

            var t = new Runnable() {

                @Override
                public void run() {

                    for (Runnable runnable : tasks) {
                        runnable.run();
                    }
                    currentTaskWorker = null;
                }
            };
            currentTaskWorker = executor.submit(t);
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
        lightmap = new LightmapNewAndCool(window.size(), resolution, isUseAntialiasing);
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
        var taskWorker = currentTaskWorker;
        if (taskWorker != null) {
            try {
                taskWorker.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Future<Sprite> submit = executor.submit(() -> lightmap.createSprite());
        window.drawSprite(submit, origin(), lightmap.resolution(), ambientLight.invert(), Angle.none());
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

}
