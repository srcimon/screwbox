package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

import static java.awt.RenderingHints.*;
import static java.util.Objects.nonNull;

public class ViewportSupport implements Updatable {

    //TODO boolean arrangeHorizontally();
    //TODO boolean arrangeVertically();

    private final WindowFrame frame;
    private final Renderer renderer;
    private final GraphicsConfiguration configuration;
    private final DefaultScreen screen;
    private final List<Viewport> viewports;
    private Graphics2D lastGraphics;
    private boolean isSplitscreenActive = false;
    DefaultViewport e1;
    DefaultViewport e2;
    public ViewportSupport(final WindowFrame frame, final Renderer renderer, final GraphicsConfiguration configuration, final DefaultScreen screen) {
        this.frame = frame;
        this.renderer = renderer;
        this.configuration = configuration;
        this.screen = screen;
        e1 = new DefaultViewport(renderer);
        e2 = new DefaultViewport(renderer);
        this.viewports = List.of(e1, e2);
    }

    public boolean isSplitscreenActive() {
        return isSplitscreenActive;
    }

    public List<Viewport> viewports() {
        return viewports;
    }

    @Override
    public void update() {
        final Supplier<Graphics2D> graphicsSupplier = () -> {
            frame.getCanvas().getBufferStrategy().show();
            final Graphics2D graphics = getDrawGraphics();
            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            graphics.setRenderingHint(KEY_DITHERING, VALUE_DITHER_DISABLE);
            graphics.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
            graphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_SPEED);
            graphics.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_SPEED);
            if (configuration.isUseAntialising()) {
                graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            }
            lastGraphics = graphics;
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, frame.getCanvasSize().width(), frame.getCanvasSize().height());
            var rotation = screen.rotation().add(screen.shake());
            if (!rotation.isNone()) {
                graphics.rotate(rotation.radians(), frame.getCanvasSize().width() / 2.0, frame.getCanvasSize().height() / 2.0);
            }
            return graphics;
        };
        screen.updateClip(new ScreenBounds(0, 0, frame.getCanvasSize().width(), frame.getCanvasSize().height()));

        e1.updateClip(new ScreenBounds(400, 0, 800, 800));
        e2.updateClip(new ScreenBounds(0, 0, 800, 800));
        renderer.updateContext(graphicsSupplier);
    }

    private Graphics2D getDrawGraphics() {
        try {
            return (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
            // avoid Component must have a valid peer while closing the Window
        } catch (IllegalStateException ignored) {
            return lastGraphics;
        }
    }

    public void setSplitscreenEnabled(boolean splitscreenEnabled) {
        this.isSplitscreenActive = splitscreenEnabled;
    }
}
