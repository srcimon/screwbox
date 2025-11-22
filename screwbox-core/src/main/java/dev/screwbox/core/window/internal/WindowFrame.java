package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.keyboard.internal.DefaultKeyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.Serial;
import java.util.ArrayList;

public class WindowFrame extends JFrame implements WindowFocusListener {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean hasFocus;

    private final Canvas canvas;
    private final Size initialSize;
    private Panel inputPanel;
    private KeyListener keyListener;

    public WindowFrame(final KeyListener keyListener, final Size initialSize) {
        setLayout(new BorderLayout());
        setIcon(defaultIcon());
        setName("ScrewBox Window");
        this.keyListener = keyListener;
        this.initialSize = initialSize;
        addWindowFocusListener(this);
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(initialSize.width(), initialSize.height()));
        add(canvas);
        inputPanel = new Panel();
        inputPanel.setLayout(new GridLayout(10, 1));
        inputPanel.setVisible(false);
        add(inputPanel, BorderLayout.EAST);
        inputPanel.addKeyListener(keyListener);
        canvas.addKeyListener(keyListener);
        addKeyListener(keyListener);
    }

    java.util.List<Component> components = new ArrayList<>();
    public void showInputs() {
        canvas.setPreferredSize(new Dimension(initialSize.width() - 200, initialSize.height()));
        components.add(new Button("Input 2"));
        components.add(new Button("Input 2"));
        for(var c : components) {
            inputPanel.add(c, BorderLayout.SOUTH);
            c.addKeyListener(keyListener);
        }
        inputPanel.setPreferredSize(new Dimension(200, initialSize.height()));
        inputPanel.setVisible(true);
        pack();
    }

    public void hideInputs() {
        inputPanel.removeAll();
        components.clear();
        canvas.setPreferredSize(new Dimension(initialSize.width(), initialSize.height()));
        inputPanel.setPreferredSize(new Dimension(0, initialSize.height()));
        canvas.requestFocus();
        inputPanel.setVisible(false);
        pack();
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
                ? initialSize
                : Size.of(bounds.width, bounds.height);
    }

    public Offset getCanvasOffset() {
        final var bounds = getBounds();
        return Offset.at(bounds.x, bounds.y - getCanvas().getBounds().height + bounds.height);
    }

    public ScreenBounds getCanvasBounds() {
        return new ScreenBounds(getCanvasOffset(), getCanvasSize());
    }

}
