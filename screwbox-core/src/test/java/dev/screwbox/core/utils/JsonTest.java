package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonTest {

    @Test
    void load_typeNull_throwsException() {
        assertThatThrownBy(() -> Json.load("", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("type must not be null");
    }

    static class NoAllArgsConstructor {
        private final String name;

        public NoAllArgsConstructor() {
            this.name = "Max";
        }
    }

    @Test
    void load_noAllArgsConstructor_throwsException() {
        assertThatThrownBy(() -> Json.load("{}", NoAllArgsConstructor.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("NoAllArgsConstructor is missing all args constructor");
    }

    static class ConstructorDoesNotMatchFields {

        private final String name;

        public ConstructorDoesNotMatchFields(int age) {
            this.name = "Max " + age;
        }
    }

    @Test
    void load_constructorDoesNotMatchFields_throwsException() {
        assertThatThrownBy(() -> Json.load("{}", ConstructorDoesNotMatchFields.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ConstructorDoesNotMatchFields is missing all args constructor");
    }

    @Test
    void load_jsonNull_throwsException() {
        assertThatThrownBy(() -> Json.load(null, String.class))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("json must not be null");
    }

    record SampleEntity(String name, String city) {

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
        assertThat(result.city()).isNull();
    }

    @Test
    void load_matchingStringAttribute_setsValue() {
        var result = Json.load("{\"name\":\"Debo\"}", SampleEntity.class);

        assertThat(result.name()).isEqualTo("Debo");
        assertThat(result.city()).isNull();
    }

    @Test
    void load_matchingStringAttributes_setsValues() {
        var result = Json.load("{\"name\":\"Max\", \"city\":\"Cologne\"}", SampleEntity.class);

        assertThat(result.name()).isEqualTo("Max");
        assertThat(result.city()).isEqualTo("Cologne");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"name\" \"Max\", \"city\":\"Cologne\"}",
        "{\"name\":\"Max\" \"city\":\"Cologne\"}",
        "{\"name\":\"Max\", \"city\":\"Cologne}",
        "{\"name\":\"Max\", \"city\":Cologne\"}",
        "{\"name\":Max\", \"city\":\"Cologne\"}",
        "{\"name\":\"Max, \"city\":\"Cologne\"}",
        "{\"name:\"Max\", \"city\":\"Cologne\"}",
        "{name\":\"Max\", \"city\":\"Cologne\"}"
    })
    void load_malformattedJson_throwsException(String json) {
        assertThatThrownBy(() -> Json.load(json, SampleEntity.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("malformatted json string:");
    }

    record SampleTypedEntity(
        Integer age, int agePrimitive,
        Boolean isNice, boolean isNicePrimitive) {
    }

    @Test
    void load_entityIsMissingIntegerProperty_defaultsToZero() {
        var result = Json.load("{}", SampleTypedEntity.class);

        assertThat(result.age()).isZero();
        assertThat(result.agePrimitive()).isZero();
    }

    @Test
    void load_entityIsMissingBooleanProperty_defaultsToFalse() {
        var result = Json.load("{}", SampleTypedEntity.class);

        assertThat(result.isNice()).isFalse();
        assertThat(result.isNicePrimitive()).isFalse();
    }

}
