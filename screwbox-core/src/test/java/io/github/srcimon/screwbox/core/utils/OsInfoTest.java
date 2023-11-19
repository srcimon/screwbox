package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OsInfoTest {

    @Test
    void isOsX_osPropertyIsOsx_isTrue() {
        System.setProperty("os.name", "Mac OS X");

        assertThat(OsInfo.isMacOs()).isTrue();
    }

    @Test
    void isOsX_osPropertyIsWindows_isFalse() {
        System.setProperty("os.name", "Windows NT");

        assertThat(OsInfo.isMacOs()).isFalse();
    }

    @Test
    void isOsX_onoOsProperty_isFalse() {
        System.clearProperty("os.name");

        assertThat(OsInfo.isMacOs()).isFalse();
    }
}
