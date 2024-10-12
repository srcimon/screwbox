package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Screen;
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
    private final Screen screen;
    private final List<Viewport> viewports;
    private Graphics2D lastGraphics;
    private boolean isSplitscreenActive = false;

    public ViewportSupport(final WindowFrame frame, final Renderer renderer, final GraphicsConfiguration configuration, final Screen screen) {
        this.frame = frame;
        this.renderer = renderer;
        this.configuration = configuration;
        this.screen = screen;
        this.viewports = List.of(new DefaultViewport(renderer), new DefaultViewport(renderer));
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
            return graphics;
        };
        renderer.updateGraphicsContext(graphicsSupplier, frame.getCanvasSize(), screen.rotation().add(screen.shake()));
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
