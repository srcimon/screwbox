package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourcesTest {

    private record JsonDemo(String name) {
    }

    @Test
    void classPathElements_returnsElementsFromClassPath() {
        List<String> elements = Resources.classPathElements();
        assertThat(elements).isNotEmpty()
                .anyMatch(element -> element.contains(File.separator + "jackson-databind" + File.separator));
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
    void loadJson_fileNameNull_throwsException() {
        assertThatThrownBy(() -> Resources.loadJson(null, String.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void loadJson_typeNull_throwsException() {
        assertThatThrownBy(() -> Resources.loadJson("tile.bmp", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type must not be null");
    }

    @Test
    void loadJson_noJson_throwsException() {
        assertThatThrownBy(() -> Resources.loadJson("tile.bmp", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tile.bmp is not a JSON-File");
    }

    @Test
    void loadJson_corruptJson_throwsException() {
        assertThatThrownBy(() -> Resources.loadJson("fake.json", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file could not be deserialized: fake.json");
    }

    @Test
    void loadJson_jsonOkay_returnsObject() {
        JsonDemo result = Resources.loadJson("real.json", JsonDemo.class);
        assertThat(result.name).isEqualTo("jason");
    }

    @Test
    void resourceExists_fileNameNull_throwsException() {
        assertThatThrownBy(() -> Resources.resourceExists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void resourceExists_resourcePresent_true() {
        var result = Resources.resourceExists("fake.json");

        assertThat(result).isTrue();
    }

    @Test
    void resourceExists_resourceAbsent_false() {
        var result = Resources.resourceExists("unknown");

        assertThat(result).isFalse();
    }
}
