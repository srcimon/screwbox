package io.github.srcimon.screwbox.core.utils;

/**
 * Gives information on the current operation system.
 */
public class OsInfo {

    /**
     * Returns true if the current operation system is osx.
     */
    public static boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }
}
