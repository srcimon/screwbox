package de.suzufa.screwbox.core.graphics.internal;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import de.suzufa.screwbox.core.Engine;

public class EngineAttachedFrame extends JFrame implements WindowListener {

    private static final long serialVersionUID = 1L;

    private final Engine engine;

    public EngineAttachedFrame(Engine engine) {
        addWindowListener(this);
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

}
