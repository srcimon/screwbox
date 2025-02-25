package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import java.util.concurrent.ExecutorService;

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
}
