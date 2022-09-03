package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.jupiter.api.Test;

class ResourcesTest {

    @Test
    void resourceFile_fileNotFound_throwsException() {
        assertThatThrownBy(() -> Resources.loadFile("unknown.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown.jpg");
    }

    @Test
    void resourceFile_fileNameIsNull_throwsException() {
        assertThatThrownBy(() -> Resources.loadFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void resourceFile_fileFound_returnsFile() {
        File file = Resources.loadFile("tile.bmp");

        assertThat(file).exists();
    }

    @Test
    void loadResource_fileFound_loadsContent() {
        byte[] content = Resources.loadBinary("tile.bmp");

        assertThat(content).hasSize(1078);
    }
}
