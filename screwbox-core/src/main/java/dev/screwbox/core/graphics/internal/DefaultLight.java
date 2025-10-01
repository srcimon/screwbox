package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.LensFlareBundle;
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
import static java.util.Objects.isNull;
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
    private LensFlare defaultLensFlare = LensFlareBundle.SHY.get();
    int scale;

    public DefaultLight(final GraphicsConfiguration configuration, final ViewportManager viewportManager, final ExecutorService executor) {
        this.configuration = configuration;
        this.viewportManager = viewportManager;
        this.executor = executor;
        this.scale = configuration.lightmapScale();
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
    public Light addConeLight(final Vector position, final Angle direction, final Angle cone, final double radius, final Color color) {
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
    public Light addExpandedLight(final Bounds area, final Color color, final double curveRadius, final boolean isFadeout) {
        autoTurnOnLight();
        for (final var lightRenderer : lightRenderers) {
            lightRenderer.addExpandedLight(area, color, curveRadius, isFadeout);
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
            final var lensFlareToUse = isNull(lensFlare) ? defaultLensFlare : lensFlare;
            for (final var lightRenderer : lightRenderers) {
                lightRenderer.addGlow(position, radius, color, lensFlareToUse);
            }
        }
        return this;
    }

    @Override
    public Light addExpandedGlow(final Bounds bounds, final double radius, final Color color, final LensFlare lensFlare) {
        autoTurnOnLight();
        if (radius != 0 && !color.opacity().isZero() && !lightPhysics.isCoveredByShadowCasters(bounds)) {
            final var lensFlareToUse = isNull(lensFlare) ? defaultLensFlare : lensFlare;
            for (final var lightRenderer : lightRenderers) {
                lightRenderer.addGlow(bounds, radius, color, lensFlareToUse);
            }
        }
        return this;
    }

    @Override
    public Light setDefaultLensFlare(final LensFlare defaultLensFlare) {
        this.defaultLensFlare = defaultLensFlare;
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
                // Avoid flickering by overdraw at last by one pixel
                final var overlap = Math.max(1, configuration.lightmapBlur()) * -scale;
                final var lights = lightRenderer.renderLight();
                lightRenderer.canvas().drawSprite(lights, Offset.at(overlap, overlap), scaled(scale).opacity(ambientLight.invert()).ignoreOverlayShader());
                lightRenderer.renderGlows();
            }
        }
        renderInProgress = false;
        return this;
    }

    public void update() {
        lightPhysics.clear();
        lightRenderers.clear();
        scale = configuration.lightmapScale();
        for (final var viewport : viewportManager.viewports()) {
            final LightRenderer renderer = new LightRenderer(lightPhysics, configuration, executor, viewport, postFilter);
            lightRenderers.add(renderer);
        }
    }

    private void autoTurnOnLight() {
        if (!configuration.isLightEnabled() && configuration.isAutoEnableLight()) {
            configuration.setLightEnabled(true);
        }
    }

}
