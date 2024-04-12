package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.awt.*;
import java.lang.management.ManagementFactory;

/**
 * Gives support for MacOs specific features.
 */
public class MacOsSupport {

    /**
     * JVM option required to support fullscreen on MacOs.
     */
    public static final String FULLSCREEN_JVM_OPTION = "--add-opens=java.desktop/com.apple.eawt=ALL-UNNAMED";

    private MacOsSupport() {
    } // hide constructor

    /**
     * Returns true if the current operation system is MacOs.
     */
    public static boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }

    /**
     * Returns true if JVM can access MacOs specific code.
     */
    public static boolean jvmCanAccessMacOsSpecificCode() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().contains(FULLSCREEN_JVM_OPTION);
    }

    /**
     * Sets the current dock image to the given Sprite. Only supports {@link Sprite}s with exactly one {@link Frame}.
     */
    public static void setDockImage(final Sprite sprite) {
        try {
            final var application = Class.forName("com.apple.eawt.Application");
            final var setDockIconImage = application.getMethod("setDockIconImage", Image.class);
            setDockIconImage.invoke(application.getConstructor().newInstance(), sprite.singleFrame().image());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Please add jvm parameters to allow setting dock image: " + MacOsSupport.FULLSCREEN_JVM_OPTION, e);
        }
    }
}
