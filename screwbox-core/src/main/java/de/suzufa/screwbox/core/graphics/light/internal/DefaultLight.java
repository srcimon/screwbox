package de.suzufa.screwbox.core.graphics.light.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.light.Light;

public class DefaultLight implements Light {

    private final ExecutorService executor;

    List<Bounds> obstacles = new ArrayList<>();

    public DefaultLight(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Light updateObstacles(final List<Bounds> obstacles) {
        this.obstacles = obstacles;
        return this;
    }

    @Override
    public List<Bounds> obstacles() {
        return obstacles;
    }

    @Override
    public Light addPointLight(Vector position, double range, Color color) {
        // TODO Auto-generated method stub
        return null;
    }

}
