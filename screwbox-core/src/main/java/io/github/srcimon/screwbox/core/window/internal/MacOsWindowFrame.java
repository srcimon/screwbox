package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.assets.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.internal.MacOsSupport;

import java.awt.*;
import java.io.Serial;

public class MacOsWindowFrame extends WindowFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    public MacOsWindowFrame(final Size initialSize) {
        super(initialSize);
    }

    @Override
    public void makeFullscreen(final GraphicsDevice graphicsDevice) {
        try {
            Thread.sleep(500); // fixes Problem when called to fast after closing a window, I am sorry!
            final var screenUtils = Class.forName("com.apple.eawt.FullScreenUtilities");
            final var canFullscreenMethod = screenUtils.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
            canFullscreenMethod.invoke(screenUtils, this, true);

            final var application = Class.forName("com.apple.eawt.Application");
            final var fullScreenMethod = application.getMethod("requestToggleFullScreen", Window.class);
            fullScreenMethod.invoke(application.getConstructor().newInstance(), this);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Please add jvm parameters to allow native fullscreen on MacOs: " + MacOsSupport.FULLSCREEN_JVM_OPTION,
                    e);
        }
    }

    public void setIcon(final Sprite sprite) {
        Taskbar.getTaskbar().setIconImage(sprite.singleFrame().image());
    }

    protected Sprite defaultIcon() {
        return SpriteBundle.ICON_512.get();
    }
}
