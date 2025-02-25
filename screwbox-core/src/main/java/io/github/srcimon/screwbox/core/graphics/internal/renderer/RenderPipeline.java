package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

//TODO add final
public class RenderPipeline implements Renderer {

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

    @Override
    public void updateContext(Supplier<Graphics2D> graphics) {
        standbyProxyRenderer.updateContext(graphics);
    }

    @Override
    public void rotate(Rotation rotation, ScreenBounds clip, Color backgroundColor) {
        standbyProxyRenderer.rotate(rotation, clip, backgroundColor);
    }

    @Override
    public void fillWith(Color color, ScreenBounds clip) {
        standbyProxyRenderer.fillWith(color, clip);
    }

    @Override
    public void fillWith(Sprite sprite, SpriteFillOptions options, ScreenBounds clip) {
        standbyProxyRenderer.fillWith(sprite, options, clip);
    }

    @Override
    public void drawText(Offset offset, String text, SystemTextDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawText(offset, text, options, clip);
    }

    @Override
    public void drawRectangle(Offset offset, Size size, RectangleDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawRectangle(offset, size, options, clip);
    }

    @Override
    public void drawLine(Offset from, Offset to, LineDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawLine(from, to, options, clip);
    }

    @Override
    public void drawCircle(Offset offset, int radius, CircleDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawCircle(offset, radius, options, clip);
    }

    @Override
    public void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawText(Offset offset, String text, TextDrawOptions options, ScreenBounds clip) {
        standbyProxyRenderer.drawText(offset, text, options, clip);
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
