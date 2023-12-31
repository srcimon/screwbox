package io.github.srcimon.screwbox.core.utils;

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
}
