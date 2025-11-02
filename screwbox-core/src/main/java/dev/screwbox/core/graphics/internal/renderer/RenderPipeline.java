package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.GraphicsConfigurationEvent;
import dev.screwbox.core.graphics.GraphicsConfigurationListener;
import dev.screwbox.core.graphics.internal.Renderer;

import java.util.concurrent.ExecutorService;

import static dev.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.OVERLAY_SHADER;

public class RenderPipeline implements GraphicsConfigurationListener {

    private final OrderingAsyncRenderer orderingAsyncRenderer;
    private final StandbyProxyRenderer standbyProxyRenderer;
    private final DefaultRenderer defaultRenderer;
    private final GraphicsConfiguration configuration;

    public RenderPipeline(final ExecutorService executor, final GraphicsConfiguration configuration, final Engine engine) {
        defaultRenderer = new DefaultRenderer();
        orderingAsyncRenderer = new OrderingAsyncRenderer(defaultRenderer, executor, engine);
        final var firewallRenderer = new FirewallRenderer(orderingAsyncRenderer);
        standbyProxyRenderer = new StandbyProxyRenderer(firewallRenderer);
        this.configuration = configuration;
        configuration.addListener(this);
    }

    public Renderer renderer() {
        return standbyProxyRenderer;
    }

    public void skipFrames(final int count) {
        standbyProxyRenderer.skipFrames(count);
    }

    public void toggleOnOff() {
        standbyProxyRenderer.toggleOnOff();
    }

    public Duration renderDuration() {
        return orderingAsyncRenderer.renderDuration();
    }

    public int renderTaskCount() {
        return orderingAsyncRenderer.renderTaskCount();
    }

    @Override
    public void configurationChanged(final GraphicsConfigurationEvent event) {
        if (event.changedProperty().equals(OVERLAY_SHADER)) {
            defaultRenderer.setDefaultShader(configuration.overlayShader());
        }
    }
}
