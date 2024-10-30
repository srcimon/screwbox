package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Light;
import io.github.srcimon.screwbox.core.graphics.internal.filter.SizeIncreasingBlurImageFilter;
import io.github.srcimon.screwbox.core.graphics.internal.filter.SizeIncreasingImageFilter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.LIGHTMAP_BLUR;
import static java.util.Objects.requireNonNull;

public class DefaultLight implements Light {

    private final LightPhysics lightPhysics = new LightPhysics();
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final List<LightDelegate> delegates = new ArrayList<>();
    private final GraphicsConfiguration configuration;
    private UnaryOperator<BufferedImage> postFilter;
    private Percent ambientLight = Percent.zero();
    private boolean renderInProgress = false;

    public DefaultLight(final GraphicsConfiguration configuration, ViewportManager viewportManager, ExecutorService executor) {
        this.configuration = configuration;
        this.viewportManager = viewportManager;
        this.executor = executor;
        updatePostFilter();
        configuration.addListener(event -> {
            if (LIGHTMAP_BLUR.equals(event.changedProperty())) {
                updatePostFilter();
            }
        });
    }

    private void updatePostFilter() {
        postFilter = configuration.lightmapBlur() == 0
                ? new SizeIncreasingImageFilter(1) // overdraw is needed to avoid issue with rotating screen
                : new SizeIncreasingBlurImageFilter(configuration.lightmapBlur());
    }

    @Override
    public Light addConeLight(final Vector position, final Rotation direction, final Rotation cone, final double radius, final Color color) {
        for (final var delegate : delegates) {
            delegate.addConeLight(position, direction, cone, radius, color);
        }
        return this;
    }

    @Override
    public Light addPointLight(final Vector position, final double radius, final Color color) {
        if (!lightPhysics.isCoveredByShadowCasters(position)) {
            for (final var delegate : delegates) {
                delegate.addPointLight(position, radius, color);
            }
        }
        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final double radius, final Color color) {
        for (final var delegate : delegates) {
            delegate.addSpotLight(position, radius, color);
        }
        return this;
    }

    @Override
    public Light addShadowCaster(final Bounds shadowCaster, final boolean selfShadow) {
        if (selfShadow) {
            lightPhysics.addShadowCaster(shadowCaster);
        } else {
            lightPhysics.addNoSelfShadowShadowCasters(shadowCaster);
        }
        return this;
    }

    @Override
    public Light addFullBrightnessArea(final Bounds area) {
        for (final var delegate : delegates) {
            delegate.addFullBrightnessArea(area);
        }
        return this;
    }

    @Override
    public Light setAmbientLight(final Percent ambientLight) {
        this.ambientLight = requireNonNull(ambientLight, "ambient light must not be null");
        for (final var delegate : delegates) {
            delegate.setAmbientLight(ambientLight);
        }
        return this;
    }

    @Override
    public Percent ambientLight() {
        return ambientLight;
    }

    @Override
    public Light addGlow(final Vector position, final double radius, final Color color) {
        if (radius != 0 && !lightPhysics.isCoveredByShadowCasters(position)) {
            for (final var delegate : delegates) {
                delegate.addGlow(position, radius, color);
            }
        }
        return this;
    }

    @Override
    public Light render() {
        if (renderInProgress) {
            throw new IllegalStateException("rendering lights is already in progress");
        }

        renderInProgress = true;
        for (final var delegate : delegates) {
            delegate.render();
        }
        renderInProgress = false;
        return this;
    }

    public void update() {
        for (final var delegate : delegates) {
            delegate.update();
        }
        lightPhysics.clear();
        delegates.clear();
        for (final var viewport : viewportManager.activeViewports()) {
            final LightDelegate delegate = new LightDelegate(lightPhysics, configuration, executor, viewport, postFilter);
            delegate.setAmbientLight(ambientLight);//TODO move to constructor?
            delegates.add(delegate);
        }
    }
}
