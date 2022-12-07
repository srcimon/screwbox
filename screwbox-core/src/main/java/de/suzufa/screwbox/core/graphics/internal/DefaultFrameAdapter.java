package de.suzufa.screwbox.core.graphics.internal;

import javax.swing.JFrame;

public class DefaultFrameAdapter implements FrameAdapter {

    private JFrame frame;

    public DefaultFrameAdapter(final JFrame frame) {
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

}
