package de.suzufa.screwbox.core.graphics.internal;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import de.suzufa.screwbox.core.Engine;

public class WindowFrame extends JFrame implements WindowListener, WindowFocusListener {

    private static final long serialVersionUID = 1L;

    private final transient Engine engine;

    private boolean hasFocus;

    public WindowFrame(Engine engine) {
        addWindowListener(this);
        addWindowFocusListener(this);
        this.engine = engine;
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
