package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

//TODO add feature buble to webpage
//TODO add package info files
//TODO blog on smoke
public class DefaultSmoke implements Smoke {

    private SmokeOptions options = SmokeOptions.vaporPreset();
    //TODO support split screen!!!!!!!!!!!!!!!!!
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final SmokeRenderer smokeRender;
    private final GraphicsConfiguration configuration;

    private final List<Bounds> obstacles = new ArrayList<>();
    private final List<DensityChange> densityChanges = new ArrayList<>();
    private final List<VelocityChange> velocityChanges = new ArrayList<>();

    private List<SmokeViewport> smokeViewports = new ArrayList<>();

    public DefaultSmoke(final ViewportManager viewportManager, final GraphicsConfiguration configuration, final ExecutorService executor, final SmokeRenderer smokeRender) {
        this.viewportManager = viewportManager;
        this.executor = executor;
        this.smokeRender = smokeRender;
        this.configuration = configuration;
    }

    @Override
    public Smoke setOptions(final SmokeOptions options) {
        this.options = Objects.requireNonNull(options, "options must not be null");
        return this;
    }

    @Override
    public Smoke emit(final Vector position, final Vector velocity, final double amount, final Color color) {
        emit(position, amount, color);
        push(position, velocity);
        return this;
    }

    @Override
    public Smoke emit(final Vector position, final double amount, final Color color) {
        Validate.zeroOrPositive(amount, "amount must be positive");
        densityChanges.add(new DensityChange(position, amount, color));
        return this;
    }

    @Override
    public Smoke push(final Vector position, final Vector velocity) {
        velocityChanges.add(new VelocityChange(position, velocity));
        return this;
    }

    @Override
    public Smoke addObstacle(final Bounds bounds) {
        obstacles.add(bounds);
        return this;
    }

    @Override
    public Smoke render(final double delta) {
        int viewportId = 0;
        for (final var viewport : viewportManager.viewports()) {
            if(smokeViewports.size() <= viewportId) {
                smokeViewports.add(new SmokeViewport(executor, viewport, configuration, smokeRender));
            }
            SmokeViewport smokeViewport = smokeViewports.get(viewportId);
            smokeViewport.render(options, delta, obstacles, densityChanges, velocityChanges);
            viewportId++;
        }
        System.out.println(smokeViewports.size());
        //TODO kill unused smokeviewports

        obstacles.clear();
        densityChanges.clear();
        velocityChanges.clear();
        //TODO move towards update?

        return this;
    }

}
