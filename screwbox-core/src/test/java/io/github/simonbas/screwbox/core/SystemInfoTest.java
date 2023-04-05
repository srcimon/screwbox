package io.github.simonbas.screwbox.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SystemInfoTest {

    @Test
    void isMacOs_noOsPropertyFound_isFalse() {
        System.clearProperty("os.name");

        assertThat(SystemInfo.isMacOs()).isFalse();
    }

    @Test
    void isMacOs_osPropertyWindowsFound_isFalse() {
        System.setProperty("os.name", "Windows");

        assertThat(SystemInfo.isMacOs()).isFalse();
    }

    @Test
    void isMacOs_osPropertyOsxFound_isTrue() {
        System.setProperty("os.name", "Mac OS X");

        assertThat(SystemInfo.isMacOs()).isTrue();
    }

    @Test
    void isJvmArgumentSet_notSet_isFalse() {
        assertThat(SystemInfo.isJvmOptionSet("-DunknownProperty=false")).isFalse();
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("os.name");
    }
}
