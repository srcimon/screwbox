package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public class DefaultFrameAdapter implements FrameAdapter {

    private WindowFrame frame;
    private GraphicsDevice graphicsDevice;

    public DefaultFrameAdapter(final WindowFrame frame) {
        this.frame = frame;
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    @Override
    public int width() {
        return frame.getWidth();
    }

    @Override
    public int height() {
        return frame.getHeight();
    }

    @Override
    public Rectangle canvasBounds() {
        return frame.getCanvas().getBounds();
    }

    @Override
    public Rectangle bounds() {
        return frame.getBounds();
    }

    @Override
    public String title() {
        return frame.getTitle();
    }

    @Override
    public void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }

    @Override
    public boolean hasFocus() {
        return frame.hasFocus();
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    @Override
    public DisplayMode displayMode() {
        return graphicsDevice.getDisplayMode();
    }

    @Override
    public Cursor createCustomCursor(Image cursor, Point hotSpot, String name) {
        return Toolkit.getDefaultToolkit().createCustomCursor(cursor, hotSpot, name);
    }

    @Override
    public DisplayMode[] displayModes() {
        return graphicsDevice.getDisplayModes();
    }

    @Override
    public void dispose() {
        frame.dispose();
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        graphicsDevice.setDisplayMode(displayMode);

    }

    @Override
    public void setFullScreenWindow(Window window) {
        graphicsDevice.setFullScreenWindow(window);
    }

    @Override
    public void setSelfAsFullscreenWindow() {
        setFullScreenWindow(frame);
    }

    @Override
    public void setLocationRelativeTo(Component component) {
        frame.setLocationRelativeTo(component);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        frame.setBounds(x, y, width, height);
    }

    @Override
    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }

    @Override
    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }

    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public void setIgnoreRepaint(boolean ignoreRepaint) {
        frame.setIgnoreRepaint(ignoreRepaint);
    }

    @Override
    public void createCanvasBufferStrategy() {
        frame.getCanvas().createBufferStrategy(2);
    }

    @Override
    public Graphics2D canvasDrawGraphics() {
        return (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
    }

    @Override
    public void showDrawGraphics() {
        frame.getCanvas().getBufferStrategy().show();
    }

    @Override
    public Insets insets() {
        return frame.getInsets();
    }

    @Override
    public int x() {
        return frame.getX();
    }

    @Override
    public int y() {
        return frame.getY();
    }

}
