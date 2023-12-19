package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MacOsSupportTest {

    @Test
    void isMacOs_osxPropertySetToMacOsX_isTrue() {
        System.setProperty("os.name", "Mac OS X");

        assertThat(MacOsSupport.isMacOs()).isTrue();
    }

    @Test
    void isMacOs_osxPropertySetToWindows_isFalse() {
        System.setProperty("os.name", "Windows NT");

        assertThat(MacOsSupport.isMacOs()).isFalse();
    }

    @Test
    void isMacOs_osxPropertyMissing_isFalse() {
        System.clearProperty("os.name");

        assertThat(MacOsSupport.isMacOs()).isFalse();
    }
}
