package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourcesTest {

    @Test
    void classPathElements_returnsElementsFromClassPath() {
        List<String> elements = Resources.classPathElements();
        assertThat(elements).isNotEmpty()
                .anyMatch(element -> element.contains(File.separator + "junit" + File.separator));
    }

    @Test
    void loadBinary_fileNotFound_throwsException() {
        assertThatThrownBy(() -> Resources.loadBinary("unknown.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown.jpg");
    }

    @Test
    void loadBinary_fileNameIsNull_throwsException() {
        assertThatThrownBy(() -> Resources.loadBinary(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void loadBinary_fileFound_loadsContent() {
        byte[] content = Resources.loadBinary("tile.bmp");

        assertThat(content).hasSize(1078);
    }

    @Test
    void resourceExists_fileNameNull_throwsException() {
        assertThatThrownBy(() -> Resources.resourceExists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void resourceExists_resourcePresent_true() {
        var result = Resources.resourceExists("fake.mid");

        assertThat(result).isTrue();
    }

    @Test
    void resourceExists_resourceAbsent_false() {
        var result = Resources.resourceExists("unknown");

        assertThat(result).isFalse();
    }

    @Test
    void loadProperties_fileNotFound_throwsException() {
        assertThatThrownBy(() -> Resources.loadProperties("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown");
    }

    @Test
    void loadProperties_propertiesFound_containsProperties() {
        var properties = Resources.loadProperties("test.properties");

       assertThat(properties).isEqualTo(Map.of("a","1","b", "2"));
    }
}
