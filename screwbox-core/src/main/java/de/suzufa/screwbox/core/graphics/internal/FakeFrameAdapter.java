package de.suzufa.screwbox.core.graphics.internal;

//TODO: allow interaction with fake frame adapter
public class FakeFrameAdapter implements FrameAdapter {

    private int width = 800;
    private int height = 600;

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

}
