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

public class DefaultLight implements Light {

    private final LightPhysics lightPhysics = new LightPhysics();
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private List<LightDelegate> delegates = new ArrayList<>();
    private final GraphicsConfiguration configuration;
    private UnaryOperator<BufferedImage> postFilter;

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
    public Light addConeLight(Vector position, Rotation direction, Rotation cone, double radius, Color color) {
        return this;
    }

    @Override
    public Light addPointLight(Vector position, double radius, Color color) {
        for (final var delegate : delegates) {
            delegate.addPointLight(position, radius, color);
        }
        return this;
    }

    @Override
    public Light addSpotLight(Vector position, double radius, Color color) {
        return this;
    }

    @Override
    public Light addShadowCaster(Bounds shadowCaster, boolean selfShadow) {
        if (selfShadow) {
            lightPhysics.addShadowCaster(shadowCaster);
        } else {
            lightPhysics.addNoSelfShadowShadowCasters(shadowCaster);
        }
        return this;
    }

    @Override
    public Light addFullBrightnessArea(Bounds area) {
        return this;
    }

    @Override
    public Light setAmbientLight(Percent ambientLight) {
        return this;
    }

    @Override
    public Percent ambientLight() {
        return null;
    }

    @Override
    public Light addGlow(Vector position, double radius, Color color) {
        return this;
    }

    @Override
    public Light render() {
        return this;
    }

    public void update() {
        System.out.println(delegates.size());
        for (var delegate : delegates) {
            delegate.update();
        }
        lightPhysics.clear();
        delegates.clear();
        for (var viewport : viewportManager.activeViewports()) {
            delegates.add(new LightDelegate(lightPhysics, configuration, executor, viewport, postFilter));
        }
    }
}
