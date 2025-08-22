package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.filter.SizeIncreasingBlurImageFilter;
import dev.screwbox.core.graphics.internal.filter.SizeIncreasingImageFilter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.LIGHTMAP_BLUR;
import static dev.screwbox.core.graphics.options.SpriteDrawOptions.scaled;
import static java.util.Objects.requireNonNull;

public class DefaultLight implements Light {

    private final LightPhysics lightPhysics = new LightPhysics();
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final List<LightRenderer> lightRenderers = new ArrayList<>();
    private final GraphicsConfiguration configuration;
    private UnaryOperator<BufferedImage> postFilter;
    private Percent ambientLight = Percent.zero();
    private boolean renderInProgress = false;
    private LensFlare defaultLensFlare = LensFlare.noRays()
            .orb(1.3, 0.5, 0.125)
            .orb(2.4, 0.2, 0.185)
            .orb(-1.5, 1.0, 0.125);

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
        autoTurnOnLight();
        for (final var viewportLight : lightRenderers) {
            viewportLight.addConeLight(position, direction, cone, radius, color);
        }
        return this;
    }

    @Override
    public Light addPointLight(final Vector position, final double radius, final Color color) {
        autoTurnOnLight();
        if (!lightPhysics.isCoveredByShadowCasters(position)) {
            for (final var lightRenderer : lightRenderers) {
                lightRenderer.addPointLight(position, radius, color);
            }
        }
        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final double radius, final Color color) {
        autoTurnOnLight();
        for (final var lightRenderer : lightRenderers) {
            lightRenderer.addSpotLight(position, radius, color);
        }
        return this;
    }

    @Override
    public Light addShadowCaster(final Bounds shadowCaster, final boolean selfShadow) {
        autoTurnOnLight();

        if (selfShadow) {
            lightPhysics.addShadowCaster(shadowCaster);
        } else {
            lightPhysics.addNoSelfShadowShadowCasters(shadowCaster);
        }
        return this;
    }

    @Override
    public Light addOrthographicWall(final Bounds bounds) {
        autoTurnOnLight();
        for (final var lightRenderer : lightRenderers) {
            lightRenderer.addOrthographicWall(bounds);
        }
        return this;
    }

    @Override
    public Light addAerialLight(final Bounds area, final Color color) {
        autoTurnOnLight();
        for (final var lightRenderer : lightRenderers) {
            lightRenderer.addAerialLight(area, color);
        }
        return this;
    }

    @Override
    public Light setAmbientLight(final Percent ambientLight) {
        autoTurnOnLight();
        this.ambientLight = requireNonNull(ambientLight, "ambient light must not be null");
        return this;
    }

    @Override
    public Percent ambientLight() {
        return ambientLight;
    }

    @Override
    public Light addGlow(final Vector position, final double radius, final Color color, final LensFlare lensFlare) {
        autoTurnOnLight();
        if (radius != 0 && !color.opacity().isZero() && !lightPhysics.isCoveredByShadowCasters(position)) {
            for (final var lightRenderer : lightRenderers) {
                lightRenderer.addGlow(position, radius, color, lensFlare);
            }
        }
        return this;
    }

    @Override
    public Light setDefaultLensFlare(final LensFlare lensFlare) {
        defaultLensFlare = requireNonNull(lensFlare, "lens flare must not be null");
        return this;
    }

    @Override
    public LensFlare defaultLensFlare() {
        return defaultLensFlare;
    }

    @Override
    public Light render() {
        if (renderInProgress) {
            throw new IllegalStateException("rendering lights is already in progress");
        }

        renderInProgress = true;
        for (final var lightRenderer : lightRenderers) {
            if (!ambientLight.isMax() && configuration.isLightEnabled()) {
                final var lights = lightRenderer.renderLight();
                // Avoid flickering by overdraw at last by one pixel
                final var overlap = Math.max(1, configuration.lightmapBlur()) * -configuration.lightmapScale();
                lightRenderer.canvas().drawSprite(lights, Offset.at(overlap, overlap), scaled(configuration.lightmapScale()).opacity(ambientLight.invert()).ignoreOverlayShader());
                lightRenderer.renderGlows();
            }
        }
        renderInProgress = false;
        return this;
    }

    public void update() {
        lightPhysics.clear();
        lightRenderers.clear();
        for (final var viewport : viewportManager.viewports()) {
            final LightRenderer viewportLight = new LightRenderer(lightPhysics, configuration, executor, viewport, postFilter);
            lightRenderers.add(viewportLight);
        }
    }

    private void autoTurnOnLight() {
        if (!configuration.isLightEnabled() && configuration.isAutoEnableLight()) {
            configuration.setLightEnabled(true);
        }
    }

}
