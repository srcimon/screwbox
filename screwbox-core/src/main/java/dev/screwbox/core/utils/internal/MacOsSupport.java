package dev.screwbox.core.utils.internal;

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
}
