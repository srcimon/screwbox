package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Canvas;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class WindowFrame extends JFrame implements WindowFocusListener {

    private static final long serialVersionUID = 1L;

    private boolean hasFocus;

    private final Canvas canvas;

    public WindowFrame() {
        JMenuBar menubar = new JMenuBar();
        JMenu c = new JMenu("df");
        c.add(new JMenuItem("asd"));
        menubar.add(c);
        setJMenuBar(menubar);
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
}
