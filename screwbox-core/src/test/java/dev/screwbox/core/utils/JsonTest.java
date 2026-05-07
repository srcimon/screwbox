package dev.screwbox.core.utils;

import net.bytebuddy.description.modifier.TypeManifestation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonTest {

    private static final String EMPTY = "{}";

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
        assertThatThrownBy(() -> Json.load(EMPTY, NoAllArgsConstructor.class))
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
        assertThatThrownBy(() -> Json.load(EMPTY, ConstructorDoesNotMatchFields.class))
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
        var entity = Json.load(EMPTY, SampleEntity.class);
        assertThat(entity.name()).isNull();
        assertThat(entity.city()).isNull();
    }

    @Test
    void load_matchingStringAttribute_setsValue() {
        var entity = Json.load("{\"name\":\"Debo\"}", SampleEntity.class);

        assertThat(entity.name()).isEqualTo("Debo");
        assertThat(entity.city()).isNull();
    }

    @Test
    void load_matchingStringAttributes_setsValues() {
        var entity = Json.load("{\"name\":\"Max\", \"city\":\"Cologne\"}", SampleEntity.class);

        assertThat(entity.name()).isEqualTo("Max");
        assertThat(entity.city()).isEqualTo("Cologne");
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
        Boolean isNice, boolean isNicePrimitive,
        TypeManifestation enumeration) {
    }

    @Test
    void load_entityIsMissingIntegerProperty_defaultsToZero() {
        var entity = Json.load(EMPTY, SampleTypedEntity.class);

        assertThat(entity.age()).isZero();
        assertThat(entity.agePrimitive()).isZero();
    }

    @Test
    void load_entityIsMissingBooleanProperty_defaultsToFalse() {
        var entity = Json.load(EMPTY, SampleTypedEntity.class);

        assertThat(entity.isNice()).isFalse();
        assertThat(entity.isNicePrimitive()).isFalse();
    }

    @Test
    void load_entityIsMissingEnumProperty_defaultsToFalse() {
        var entity = Json.load(EMPTY, SampleTypedEntity.class);

        assertThat(entity.enumeration()).isNull();
    }

    @Test
    void load_entityHasEnumProperty_setsValue() {
        var entity = Json.load("{\"enumeration\": \"FINAL\"}", SampleTypedEntity.class);

        assertThat(entity.enumeration()).isEqualTo(TypeManifestation.FINAL);
    }

}
