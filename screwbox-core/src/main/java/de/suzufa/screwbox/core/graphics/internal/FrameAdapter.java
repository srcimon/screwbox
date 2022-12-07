package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Cursor;
import java.awt.Rectangle;

public interface FrameAdapter {

    int width();

    int height();

    Rectangle canvasBounds();

    Rectangle bounds();

    String title();

    void setCursor(Cursor cursor);
}
