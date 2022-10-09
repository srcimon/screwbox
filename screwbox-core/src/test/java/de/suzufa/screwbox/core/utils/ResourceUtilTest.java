package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ResourceUtilTest {

    private record JsonDemo(String name) {
    }

    @Test
    void loadBinary_fileNotFound_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadBinary("unknown.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown.jpg");
    }

    @Test
    void loadBinary_fileNameIsNull_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadBinary(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void loadBinary_fileFound_loadsContent() {
        byte[] content = ResourceUtil.loadBinary("tile.bmp");

        assertThat(content).hasSize(1078);
    }

    @Test
    void loadJson_fileNameNull_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadJson(null, String.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void loadJson_typeNull_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadJson("tile.bmp", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type must not be null");
    }

    @Test
    void loadJson_noJson_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadJson("tile.bmp", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tile.bmp is not a JSON-File");
    }

    @Test
    void loadJson_corruptJson_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.loadJson("fake.json", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file could not be deserialized: fake.json");
    }

    @Test
    void loadJson_jsonOkay_returnsObject() {
        JsonDemo result = ResourceUtil.loadJson("real.json", JsonDemo.class);
        assertThat(result.name).isEqualTo("jason");
    }

    @Test
    void resourceExists_fileNameNull_throwsException() {
        assertThatThrownBy(() -> ResourceUtil.resourceExists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void resourceExists_resourcePresent_true() {
        var result = ResourceUtil.resourceExists("fake.json");

        assertThat(result).isTrue();
    }

    @Test
    void resourceExists_resourceAbsent_false() {
        var result = ResourceUtil.resourceExists("unknown");

        assertThat(result).isFalse();
    }
}
