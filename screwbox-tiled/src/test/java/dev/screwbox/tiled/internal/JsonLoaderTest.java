package dev.screwbox.tiled.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonLoaderTest {

    private record JsonDemo(String name) {
    }

    @Test
    void loadJson_fileNameNull_throwsException() {
        assertThatThrownBy(() -> JsonLoader.loadJson(null, String.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void loadJson_typeNull_throwsException() {
        assertThatThrownBy(() -> JsonLoader.loadJson("tile.bmp", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type must not be null");
    }

    @Test
    void loadJson_noJson_throwsException() {
        assertThatThrownBy(() -> JsonLoader.loadJson("tile.bmp", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tile.bmp is not a JSON-File");
    }

    @Test
    void loadJson_corruptJson_throwsException() {
        assertThatThrownBy(() -> JsonLoader.loadJson("fake.json", String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file could not be deserialized: fake.json");
    }

    @Test
    void loadJson_jsonOkay_returnsObject() {
        JsonDemo result = JsonLoader.loadJson("real.json", JsonDemo.class);
        assertThat(result.name).isEqualTo("jason");
    }
}
