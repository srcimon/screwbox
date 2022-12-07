package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public interface FrameAdapter {

    int width();

    int height();

    Rectangle canvasBounds();

    Rectangle bounds();

    String title();

    void setCursor(Cursor cursor);

    boolean hasFocus();

    boolean isVisible();

    void setTitle(String title);

    DisplayMode displayMode();

    Cursor createCustomCursor(Image cursor, Point hotSpot, String name);

    DisplayMode[] displayModes();

    void dispose();

    void setDisplayMode(DisplayMode displayMode);

    void setFullScreenWindow(Window window);

    void setSelfAsFullscreenWindow();

    void setLocationRelativeTo(Component component);

    void setBounds(int x, int y, int width, int height);
}
