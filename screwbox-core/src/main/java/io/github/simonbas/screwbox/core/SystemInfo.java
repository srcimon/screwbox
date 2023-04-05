package io.github.simonbas.screwbox.core;

import java.lang.management.ManagementFactory;

class SystemInfo {

    private SystemInfo() {
    }

    static boolean isMacOs() {
        return "Mac OS X".equalsIgnoreCase(System.getProperty("os.name", "UNKNOWN-OS"));
    }

    static boolean isJvmOptionSet(final String option) {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().contains(option);
    }

}
