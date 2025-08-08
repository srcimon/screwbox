package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Creates an image and draws text upon it. The first time this is done costs
 * around half a second. To speed up the engines start this should be called in
 * another thread.
 */
public class InitializeFontDrawingTask implements Runnable {

    @Override
    public void run() {
        final BufferedImage bufferedImage = ImageOperations.createEmpty(Size.square(10));
        final Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.drawString("i speed drawing with system fonts", 0, 10);
        graphics.dispose();
    }

}