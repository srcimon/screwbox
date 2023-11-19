package io.github.srcimon.screwbox.core.utils;

public class OsInfo {

    public static boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }
}
