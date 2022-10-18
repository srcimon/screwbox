package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.graphics.Offset.origin;

import java.util.List;
import java.util.concurrent.ExecutorService;

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
            List<Offset> area = lightPhysics.calculateArea(lightBox);
            lightmap.addPointLight(world.toOffset(position), world.toDistance(range), area, color);
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
        Sprite sprite = lightmap.createSprite();
        window.drawSprite(sprite, origin(), lightmap.resolution(), ambientLight.invert(), Angle.none());
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
