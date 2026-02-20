package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;

/**
 * Creates an image and draws text upon it. The first time this is done costs
 * around half a second. To speed up the engine start this should be called in
 * another thread.
 */
public class InitializeFontDrawingTask implements Runnable {

    @Override
    public void run() {
        final var image = ImageOperations.createImage(Size.square(10));
        final var graphics = image.createGraphics();
        graphics.drawString("i speed drawing with system fonts", 0, 10);
        graphics.dispose();
    }

}