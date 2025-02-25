package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.util.concurrent.ExecutorService;

//TODO add final
public class RenderPipeline {

    final DefaultRenderer defaultRenderer;
    final AsyncRenderer asyncRenderer;
    final FirewallRenderer firewallRenderer;
    final StandbyProxyRenderer standbyProxyRenderer;

    public RenderPipeline(ExecutorService executor) {
        defaultRenderer = new DefaultRenderer();
        asyncRenderer = new AsyncRenderer(defaultRenderer, executor);
        firewallRenderer = new FirewallRenderer(asyncRenderer);
        standbyProxyRenderer = new StandbyProxyRenderer(firewallRenderer);
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
}
