package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Window;
import java.lang.reflect.Method;

public class FullscreenTest {

    public static void makeFullscreen(Window window) {
        try {
            Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Method method = util.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
            method.invoke(util, window, true);

            Class<?> application = Class.forName("com.apple.eawt.Application");
            Method fullScreenMethod = application.getMethod("requestToggleFullScreen", Window.class);
            Object applicationObject = application.getConstructor().newInstance();
            fullScreenMethod.invoke(applicationObject, window);
        } catch (Exception e) {
            throw new IllegalStateException("did not function", e);
        }
    }

}
