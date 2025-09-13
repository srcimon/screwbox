package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.Serial;

import static java.util.Objects.isNull;

public class WindowFrame extends JFrame implements WindowFocusListener {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean hasFocus;

    private final Canvas canvas;
    private final Size initialSize;

    public WindowFrame(final Size initialSize) {
        setIcon(defaultIcon());
        setName("ScrewBox Window");
        this.initialSize = initialSize;
        addWindowFocusListener(this);
        canvas = new Canvas();
        add(canvas);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    @Override
    public void windowGainedFocus(final WindowEvent e) {
        this.hasFocus = true;

    }

    @Override
    public void windowLostFocus(final WindowEvent e) {
        this.hasFocus = false;
    }

    /**
     * 10 times faster than {@link JFrame#hasFocus()}
     */
    @Override
    public boolean hasFocus() {
        return hasFocus;
    }

    public int canvasHeight() {
        return getCanvas().getBounds().height;
    }

    public void makeFullscreen(final GraphicsDevice graphicsDevice) {
        graphicsDevice.setFullScreenWindow(this);
    }

    public void setIcon(final Sprite sprite) {
        setIconImage(sprite.singleImage());
    }

    protected Sprite defaultIcon() {
        return SpriteBundle.ICON.get();
    }

    public Size getCanvasSize() {
        final var bounds = getCanvas().getBounds();
        return bounds.width == 0
                ? initialSize
                : Size.of(bounds.width, bounds.height);
    }

    public Offset getCanvasScreenOffset() {
        final int menuBarHeight = isNull(getJMenuBar()) ? 0 : getJMenuBar().getHeight();
        return Offset.at(getX(), getY() + getInsets().top + menuBarHeight);
    }
}
