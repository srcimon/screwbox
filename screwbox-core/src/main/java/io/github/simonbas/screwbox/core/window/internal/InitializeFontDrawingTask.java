package io.github.simonbas.screwbox.core.window.internal;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Creates an image and draws text upon it. The first time this is done costs
 * around 0.4 sec. To speed up the engines start this should be called in
 * another thread.
 *
 */
class InitializeFontDrawingTask implements Runnable {

    @Override
    public void run() {
        final BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.drawString("i speed drawing with system fonts", 0, 10);
        graphics.dispose();
    }

}
