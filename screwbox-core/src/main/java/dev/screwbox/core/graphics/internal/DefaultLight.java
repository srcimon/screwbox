package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.LensFlareBundle;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.filter.ExpandImageFilter;
import dev.screwbox.core.graphics.options.OccluderOptions;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.utils.Validate;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

import static dev.screwbox.core.graphics.GraphicsConfiguration.DEFAULT_RESOLUTION;
import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_BLUR;
import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_QUALITY;
import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION;
import static dev.screwbox.core.graphics.options.SpriteDrawOptions.scaled;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class DefaultLight implements Light, Updatable {

    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final List<LightRenderer> renderers = new ArrayList<>();
    private final GraphicsConfiguration configuration;
    private LightPhysics lightPhysics = new LightPhysics();
    private UnaryOperator<BufferedImage> postFilter;
    private Percent ambientLight = Percent.zero();
    private boolean renderInProgress = false;
    private LensFlare defaultLensFlare = LensFlareBundle.SHY.get();
    private int scale = 4;

    public DefaultLight(final GraphicsConfiguration configuration, final ViewportManager viewportManager, final ExecutorService executor) {
        this.configuration = configuration;
        this.viewportManager = viewportManager;
        this.executor = executor;
        updatePostFilter();

        configuration.addListener(event -> {
            if (LIGHT_BLUR.equals(event.changedProperty())) {
                updatePostFilter();
            } else if (LIGHT_QUALITY.equals(event.changedProperty()) || RESOLUTION.equals(event.changedProperty())) {
                final double targetPixelCount = DEFAULT_RESOLUTION.height() * configuration.lightQuality().value();
                final double uncappedScale = configuration.resolution().height() / targetPixelCount;
                scale = (int) Math.clamp(uncappedScale, 1.0, 64.0);
            }
        });
    }

    private void updatePostFilter() {
        postFilter = configuration.lightmapBlur() == 0
            ? new ExpandImageFilter(1) // overdraw is needed to avoid issue with rotating screen
            : bufferedImage -> {
            ImageOperations.blurImage(bufferedImage, configuration.lightmapBlur());
            return new ExpandImageFilter(1).apply(bufferedImage);
        };
    }

    @Override
    public Light addDirectionalLight(final Line source, final double distance, final Color color) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addDirectionalLight(source, distance, color);
        }
        return this;
    }

    @Override
    public Light addConeLight(final Vector position, final Angle direction, final Angle cone, final double radius, final Color color) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addConeLight(position, direction, cone, radius, color);
        }
        return this;
    }

    @Override
    public Light addConeGlow(final Vector position, final Angle direction, final Angle cone, final double radius, final Color color) {
        autoTurnOnLight();
        if (radius != 0 && color.isVisible() && !cone.isZero()) {
            for (final var renderer : renderers) {
                renderer.addConeGlow(position, direction, cone, radius, color);
            }
        }
        return this;
    }

    @Override
    public Light addPointLight(final Vector position, final double radius, final Color color) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addPointLight(position, radius, color);
        }
        return this;
    }

    @Override
    public Light addSpotLight(final Vector position, final double radius, final Color color) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addSpotLight(position, radius, color);
        }
        return this;
    }

    @Override
    public Light addOccluder(final Bounds occluder, final boolean isAffectedByShadow) {
        autoTurnOnLight();

        if (isAffectedByShadow) {
            lightPhysics.addAffectedByShadowOccluder(occluder);
        } else {
            lightPhysics.addOccluder(occluder);
        }
        return this;
    }

    @Override
    public Light addBackgdropOccluder(final Polygon occluder, final OccluderOptions options) {
        autoTurnOnLight();
        Validate.isTrue(occluder::isClosed, "occluder must be closed");

        for (final var renderer : renderers) {
            renderer.addBackgdropOccluder(occluder, options);
        }
        return this;
    }

    @Override
    public Light addOrthographicWall(final Bounds bounds) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addOrthographicWall(bounds);
        }
        return this;
    }

    @Override
    public Light addAreaLight(final Bounds area, final Color color, final double curveRadius, final boolean isFadeout) {
        autoTurnOnLight();
        for (final var renderer : renderers) {
            renderer.addAreaLight(area, color, curveRadius, isFadeout);
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
        if (radius != 0 && color.isVisible()) {
            final var lensFlareToUse = isNull(lensFlare) ? defaultLensFlare : lensFlare;
            for (final var lightRenderer : renderers) {
                lightRenderer.addGlow(position, radius, color, lensFlareToUse);
            }
        }
        return this;
    }

    @Override
    public Light addAreaGlow(final Bounds bounds, final double radius, final Color color, final LensFlare lensFlare) {
        autoTurnOnLight();
        if (radius != 0 && color.isVisible()) {
            final var lensFlareToUse = isNull(lensFlare) ? defaultLensFlare : lensFlare;
            for (final var lightRenderer : renderers) {
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
        if (configuration.isLightEnabled()) {
            for (final var lightRenderer : renderers) {
                // Avoid flickering by overdraw at last by one pixel
                if (!ambientLight.isMax()) {
                    final var overlap = -lightRenderer.scale();
                    final var light = lightRenderer.renderLight();
                    lightRenderer.canvas().drawSprite(light, Offset.at(overlap, overlap), scaled(lightRenderer.scale())
                        .opacity(ambientLight.invert())
                        .ignoreOverlayShader());
                }
                lightRenderer.renderGlows();
            }
        }
        renderInProgress = false;
        return this;
    }

    @Override
    public int scale() {
        return scale;
    }

    @Override
    public void update() {
        lightPhysics = new LightPhysics();
        renderers.clear();
        for (final var viewport : viewportManager.viewports()) {
            renderers.add(createLightRender(viewport));
        }
    }

    private LightRenderer createLightRender(final Viewport viewport) {
        final var lightmap = new Lightmap(viewport.canvas().size(), scale, configuration.lightFalloff());
        return new LightRenderer(lightPhysics, executor, viewport, configuration.isLensFlareEnabled(), lightmap, postFilter);
    }

    private void autoTurnOnLight() {
        if (!configuration.isLightEnabled() && configuration.isAutoEnableLight()) {
            configuration.setLightEnabled(true);
        }
    }
}
