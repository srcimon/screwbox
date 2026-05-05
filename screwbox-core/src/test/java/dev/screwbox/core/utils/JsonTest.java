package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonTest {

    record SampleEntity(String name) {

    }

    static class NoAllArgsConstructor {
        private final String name;

        public NoAllArgsConstructor() {
            this.name = "Max";
        }
    }

    static class ConstructorDoesNotMatchFields {

        private final String name;

        public ConstructorDoesNotMatchFields(int age) {
            this.name = "Max " + age;
        }
    }

    @Test
    void load_jsonNull_throwsException() {
        assertThatThrownBy(() -> Json.load(null, String.class))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("json must not be null");
    }

    @Test
    void load_noAllArgsConstructor_throwsException() {
        assertThatThrownBy(() -> Json.load("{}", NoAllArgsConstructor.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("NoAllArgsConstructor is missing all args constructor");
    }

    @Test
    void load_constructorDoesNotMatchFields_throwsException() {
        assertThatThrownBy(() -> Json.load("{}", ConstructorDoesNotMatchFields.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ConstructorDoesNotMatchFields is missing all args constructor");
    }

    @Test
    void load_typeNull_throwsException() {
        assertThatThrownBy(() -> Json.load("", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("type must not be null");
    }

    @Test
    void load_emptyString_throwsException() {
        assertThatThrownBy(() -> Json.load("", SampleEntity.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("input is no json string");
    }

    @Test
    void load_emptyJson_leavesAttributesNull() {
        var result = Json.load("{}", SampleEntity.class);
        assertThat(result.name()).isNull();
    }

    @Test
    void load_matchingStringAttribute_setsValue() {
        var result = Json.load("{\"name\":\"Max\"}", SampleEntity.class);

        assertThat(result.name()).isEqualTo("Max");
    }
}
