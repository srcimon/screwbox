package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

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

}
