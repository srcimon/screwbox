package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Rectangle;

//TODO: allow interaction with fake frame adapter
public class FakeFrameAdapter implements FrameAdapter {

    private Rectangle bounds = new Rectangle(100, 150, 800, 540);
    private Rectangle canvasBounds = new Rectangle(0, 0, 800, 540);

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

}
