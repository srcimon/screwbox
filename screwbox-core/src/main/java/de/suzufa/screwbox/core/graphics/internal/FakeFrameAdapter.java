package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

//TODO: allow interaction with fake frame adapter
public class FakeFrameAdapter implements FrameAdapter {

    private String title = "";
    private Rectangle bounds = new Rectangle(100, 150, 800, 540);
    private Rectangle canvasBounds = new Rectangle(0, 0, 800, 540);
    private boolean hasFocus = true;
    private boolean isVisible = false;
    private DisplayMode displayMode = new DisplayMode(1024, 768, 32, 60);

    @Override
    public int width() {
        return bounds.x;
    }

    @Override
    public int height() {
        return bounds.y;
    }

    @Override
    public Rectangle canvasBounds() {
        return canvasBounds;
    }

    @Override
    public Rectangle bounds() {
        return bounds;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void setCursor(Cursor cursor) {
        // does nothing
    }

    @Override
    public boolean hasFocus() {
        return hasFocus;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public DisplayMode displayMode() {
        return displayMode;
    }

    @Override
    public Cursor createCustomCursor(Image cursor, Point hotSpot, String name) {
        return new Cursor(Cursor.DEFAULT_CURSOR);
    }

    @Override
    public DisplayMode[] displayModes() {
        DisplayMode[] modes = new DisplayMode[4];
        modes[0] = new DisplayMode(1024, 768, 16, 60);
        modes[1] = new DisplayMode(1024, 768, 32, 60);
        modes[2] = new DisplayMode(800, 600, 16, 60);
        modes[3] = new DisplayMode(800, 600, 32, 60);
        return modes;
    }

    @Override
    public void dispose() {
        // does nothing
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;

    }

    @Override
    public void setFullScreenWindow(Window window) {
        // does nothing
    }

    @Override
    public void setSelfAsFullscreenWindow() {
        // does nothing
    }

    @Override
    public void setLocationRelativeTo(Component component) {
        // does nothing
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

}
