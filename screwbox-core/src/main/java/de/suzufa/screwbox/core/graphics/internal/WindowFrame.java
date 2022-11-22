package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private final Canvas canvas;

    public WindowFrame(final Engine engine) {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu game = new JMenu("Game");
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                engine.stop();
            }
        });
        game.add(exit);
        menuBar.add(game);
        setJMenuBar(menuBar);
        addWindowListener(this);
        addWindowFocusListener(this);
        canvas = new Canvas();
        add(canvas);

        this.engine = engine;
    }

    public void toggleMenuBar() {
        getJMenuBar().setVisible(!getJMenuBar().isVisible());
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
}
