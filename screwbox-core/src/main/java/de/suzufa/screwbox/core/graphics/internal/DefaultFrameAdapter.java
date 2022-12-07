package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Cursor;
import java.awt.Rectangle;

public class DefaultFrameAdapter implements FrameAdapter {

    private WindowFrame frame;

    public DefaultFrameAdapter(final WindowFrame frame) {
        this.frame = frame;
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

}
