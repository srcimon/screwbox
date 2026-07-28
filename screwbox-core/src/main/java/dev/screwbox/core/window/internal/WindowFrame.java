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
import java.awt.image.BufferedImage;
import java.io.Serial;

public class WindowFrame extends JFrame implements WindowFocusListener {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean hasFocus;

    private final Canvas canvas;
    private final Size initialSize;
    private transient Robot robot;

    public WindowFrame(final Robot robot, final Size initialSize) {
        addNotify(); // forces window calculate insets
        setIcon(defaultIcon());
        setName("ScrewBox Window");
        this.initialSize = initialSize;
        this.robot = robot;
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
            ? Size.of(initialSize.width() - getInsets().left - getInsets().right, initialSize.height() - getInsets().top - getInsets().bottom)
            : Size.of(bounds.width, bounds.height);
    }

    public Offset getCanvasOffset() {
        final var bounds = getBounds();
        return Offset.at(bounds.x, bounds.y - getCanvas().getBounds().height + bounds.height);
    }

    public ScreenBounds getCanvasBounds() {
        return new ScreenBounds(getCanvasOffset(), getCanvasSize());
    }

    public BufferedImage createScreenCapture() {
        final var canvasOffset = getCanvasOffset();
        final var rectangle = new Rectangle(canvasOffset.x(), canvasOffset.y(), canvas.getWidth(), canvas.getHeight());
        return robot.createScreenCapture(rectangle);
    }
}
