package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
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
        autoTurnOnSmoke();
        densityChanges.add(new DensityChange(position, amount, color));
        return this;
    }

    @Override
    public Smoke push(final Vector position, final Vector velocity) {
        autoTurnOnSmoke();
        velocityChanges.add(new VelocityChange(position, velocity));
        return this;
    }

    @Override
    public Smoke addObstacle(final Bounds bounds) {
        autoTurnOnSmoke();
        obstacles.add(bounds);
        return this;
    }

    @Override
    public Smoke render(final double delta) {
        if (configuration.isSmokeEnabled()) {
            if (smokeViewports.size() != viewportManager.viewports().size()) {
                smokeViewports.clear();
                for (var viewport : viewportManager.viewports()) {
                    smokeViewports.add(new SmokeViewport(executor, viewport, configuration, smokeRender));
                }
            }
            for (final var smokeViewport : smokeViewports) {
                smokeViewport.render(options, delta, obstacles, densityChanges, velocityChanges);
            }
        }
        obstacles.clear();
        densityChanges.clear();
        velocityChanges.clear();
        return this;
    }

    static Scheduler scheduler = Scheduler.withInterval(Duration.ofSeconds(5));
    private void autoTurnOnSmoke() {
        if (!configuration.isSmokeEnabled() && configuration.isAutoEnableSmoke()) {
            configuration.setSmokeEnabled(true);
        }
    }
}
