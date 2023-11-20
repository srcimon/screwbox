package io.github.srcimon.screwbox.core.utils;

/**
 * Gives support for MacOs specific features.
 */
public class MacOsSupport {

    /**
     * Returns true if the current operation system is MacOs.
     */
    public static boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }
}
