package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationListener;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.OVERLAY_SHADER;

public class RenderPipeline implements GraphicsConfigurationListener {

    private final AsyncRenderer asyncRenderer;
    private final StandbyProxyRenderer standbyProxyRenderer;
    private final DefaultRenderer defaultRenderer;
    private final GraphicsConfiguration configuration;

    public RenderPipeline(final ExecutorService executor, final GraphicsConfiguration configuration) {
        defaultRenderer = new DefaultRenderer();
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
        if (event.changedProperty().equals(OVERLAY_SHADER)) {
            defaultRenderer.setDefaultShader(configuration.overlayShader());
        }

    }
}
