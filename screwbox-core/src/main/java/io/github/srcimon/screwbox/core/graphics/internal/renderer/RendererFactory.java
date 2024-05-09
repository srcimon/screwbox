package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.util.concurrent.ExecutorService;

public class RendererFactory {

    private final ExecutorService executor;

    public RendererFactory(final ExecutorService executor) {
        this.executor = executor;
    }
    public Renderer createRenderer() {
        final DefaultRenderer defaultRenderer = new DefaultRenderer();
        final AsyncRenderer asyncRenderer = new AsyncRenderer(defaultRenderer, executor);
        return new FirewallRenderer(asyncRenderer);
    }
}
