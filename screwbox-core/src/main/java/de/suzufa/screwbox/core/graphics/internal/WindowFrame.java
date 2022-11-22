package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Canvas;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.suzufa.screwbox.core.Engine;

public class WindowFrame extends JFrame implements WindowListener, WindowFocusListener {

    private static final long serialVersionUID = 1L;

    private final transient Engine engine;

    private boolean hasFocus;

    private Canvas canvas;

    public WindowFrame(Engine engine) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Debug");
        JMenuItem exit = new JMenuItem("exit");
        menu.add(exit);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        addWindowListener(this);
        addWindowFocusListener(this);
        canvas = new Canvas();
        add(canvas);
        this.engine = engine;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    @Override
    public void windowOpened(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        engine.stop();
    }

    @Override
    public void windowClosed(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowIconified(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowActivated(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        this.hasFocus = true;

    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        this.hasFocus = false;
    }

    /**
     * 10 times faster than {@link JFrame#hasFocus()}
     */
    @Override
    public boolean hasFocus() {
        return hasFocus;
    }
}
