package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationListener;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.graphics.internal.ShaderResolver;

import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.OVERLAY_SHADER;
import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.SHADER_RESOLVE_MODE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class RenderPipeline implements GraphicsConfigurationListener {

    private final AsyncRenderer asyncRenderer;
    private final StandbyProxyRenderer standbyProxyRenderer;
    private final DefaultRenderer defaultRenderer;
    private final GraphicsConfiguration configuration;

    public RenderPipeline(final ExecutorService executor, final GraphicsConfiguration configuration) {
        defaultRenderer = new DefaultRenderer();
        defaultRenderer.setDefaultShader(null, this::stackOverlayOnCustom);
        asyncRenderer = new AsyncRenderer(defaultRenderer, executor);
        FirewallRenderer firewallRenderer = new FirewallRenderer(asyncRenderer);
        standbyProxyRenderer = new StandbyProxyRenderer(firewallRenderer);
        this.configuration = configuration;
        configuration.addListener(this);
    }

    public Renderer renderer() {
        return standbyProxyRenderer;
    }

    public void skipFrames() {
        standbyProxyRenderer.skipFrames();
    }

    public void toggleOnOff() {
        standbyProxyRenderer.toggleOnOff();
    }

    public Duration renderDuration() {
        return asyncRenderer.renderDuration();
    }

    //TODO simply support a single shader mode?
    @Override
    public void configurationChanged(final GraphicsConfigurationEvent event) {
        if (event.changedProperty().equals(OVERLAY_SHADER) || event.changedProperty().equals(SHADER_RESOLVE_MODE)) {
            final ShaderResolver shaderResolver = switch (configuration.shaderResolveMode()) {
                case STACK_OVERLAY_ON_CUSTOM -> this::stackOverlayOnCustom;
                case STACK_CUSTOM_ON_OVERLAY -> this::stackCustomOnOverlay;
                case CUSTOM_PRIORITIZED -> (custom, overlay) -> nonNull(custom) ? custom : overlay;
                case OVERLAY_PRIORITIZED -> (custom, overlay) -> nonNull(overlay) ? overlay : custom;
            };
            defaultRenderer.setDefaultShader(configuration.overlayShader(), shaderResolver);
        }
    }

    public ShaderSetup stackCustomOnOverlay(final ShaderSetup overlayShader, final ShaderSetup customShader) {
        if (isNull(customShader)) {
            return overlayShader;
        }
        return isNull(overlayShader) ? null
                : ShaderSetup.combinedShader(customShader.shader(), overlayShader.shader())
                .ease(customShader.ease())
                .duration(customShader.duration())
                .offset(customShader.offset());
    }

    public ShaderSetup stackOverlayOnCustom(final ShaderSetup overlayShader, final ShaderSetup customShader) {
        if (isNull(overlayShader)) {
            return customShader;
        }
        return isNull(customShader) ? null
                : ShaderSetup.combinedShader(overlayShader.shader(), customShader.shader())
                .ease(overlayShader.ease())
                .duration(overlayShader.duration())
                .offset(overlayShader.offset());
    }
}
