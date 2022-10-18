package de.suzufa.screwbox.core.graphics.light.internal;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.light.Light;

public class DefaultLight implements Light {

    private final ExecutorService executor;

    private List<Bounds> obstacles = new ArrayList<>();

    public DefaultLight(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Light updateObstacles(final List<Bounds> obstacles) {
        this.obstacles = requireNonNull(obstacles, "obstacles must not be null");
        return this;
    }

    @Override
    public List<Bounds> obstacles() {
        return obstacles;
    }

    @Override
    public Light addPointLight(final Vector position, final double range, final Color color) {
        // TODO Auto-generated method stub
        return null;
    }

}
