package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

//TODO add feature buble to webpage
//TODO add package info files
//TODO blog on smoke
public class DefaultSmoke implements Smoke {

    private SmokeOptions options = SmokeOptions.noFade();
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final SmokeRenderer smokeRender;
    private final GraphicsConfiguration configuration;

    private final List<Bounds> obstacles = new ArrayList<>();
    private final List<DensityChange> densityChanges = new ArrayList<>();
    private final List<VelocityChange> velocityChanges = new ArrayList<>();
    private final List<VelocityZone> velocityZones = new ArrayList<>();

    private List<SmokeProjector> smokeProjectors = new ArrayList<>();

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
        if (amount > 0) {
            densityChanges.add(new DensityChange(position, amount, color));
        }
        return this;
    }

    @Override
    public Smoke push(final Vector position, final Vector velocity) {
        autoTurnOnSmoke();
        if (!velocity.isZero()) {
            velocityChanges.add(new VelocityChange(position, velocity));
        }
        return this;
    }

    @Override
    public Smoke pinVelocity(final Bounds area, final Vector velocity) {
        autoTurnOnSmoke();
        if (!velocity.isZero()) {
            velocityZones.add(new VelocityZone(area, velocity));
        }
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
            while (smokeProjectors.size() < viewportManager.viewports().size()) {
                smokeProjectors.add(new SmokeProjector(executor, configuration, smokeRender));
            }
            while (smokeProjectors.size() > viewportManager.viewports().size()) {
                smokeProjectors.removeLast();
            }
            int viewportId = 0;
            for (final var viewport : viewportManager.viewports()) {
                final var projector = smokeProjectors.get(viewportId);
                projector.adaptToViewport(viewport, options.velocity());
                projector.applyObstacles(obstacles);
                projector.applyDensityChanges(densityChanges);
                projector.applyVelocityChanges(velocityChanges);
                projector.applyVelocityZones(velocityZones);
                projector.render(viewport, options, delta);

                viewportId++;
            }
        }
        obstacles.clear();
        densityChanges.clear();
        velocityChanges.clear();
        velocityZones.clear();
        return this;
    }

    private void autoTurnOnSmoke() {
        if (!configuration.isSmokeEnabled() && configuration.isAutoEnableSmoke()) {
            configuration.setSmokeEnabled(true);
        }
    }
}
