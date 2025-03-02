package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.DefaultShaderMode;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.util.concurrent.ExecutorService;

public class RenderPipeline {

    private final AsyncRenderer asyncRenderer;
    private final StandbyProxyRenderer standbyProxyRenderer;
    private final DefaultRenderer defaultRenderer;

    public RenderPipeline(final ExecutorService executor) {
        defaultRenderer = new DefaultRenderer();
        asyncRenderer = new AsyncRenderer(defaultRenderer, executor);
        FirewallRenderer firewallRenderer = new FirewallRenderer(asyncRenderer);
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

    public void setDefaultShader(final ShaderSetup shader, final DefaultShaderMode defaultShaderMode) {
        defaultRenderer.setDefaultShader(shader, defaultShaderMode);
    }
}
