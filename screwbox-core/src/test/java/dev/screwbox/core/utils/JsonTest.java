package dev.screwbox.core.utils;

import net.bytebuddy.description.modifier.TypeManifestation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

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
            .hasMessage("input is no json string: ");
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
    void load_emptyStringAttribute_setsEmptyString() {
        var entity = Json.load("{\"name\":\"\"}", SampleEntity.class);

        assertThat(entity.name()).isEmpty();
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
        "{\"name\":\"Max, \"city\":\"Cologne\"}",
        "{\"name:\"Max\", \"city\":\"Cologne\"}",
        "{name\":\"Max\", \"city\":\"Cologne\"}",
        "{\"name\":}"
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
    void load_entityHasBooleanProperty_setsBoolean() {
        var entity = Json.load("{\"isNice\": TRUE, \"isNicePrimitive\": true }", SampleTypedEntity.class);

        assertThat(entity.isNice()).isTrue();
        assertThat(entity.isNicePrimitive()).isTrue();
    }

    @Test
    void load_entityIsMissingEnumProperty_defaultsToFalse() {
        var entity = Json.load(EMPTY, SampleTypedEntity.class);

        assertThat(entity.enumeration()).isNull();
    }

    @Test
    void load_entityHasEnumProperty_setsValue() {
        var entity = Json.load("{\"enumeration\": FINAL}", SampleTypedEntity.class);

        assertThat(entity.enumeration()).isEqualTo(TypeManifestation.FINAL);
    }

    @Test
    void load_entityHasUnknownEnumProperty_throwsException() {
        assertThatThrownBy(() -> Json.load("{\"enumeration\": UNKNOWN_PROPERTY}", SampleTypedEntity.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No enum constant net.bytebuddy.description.modifier.TypeManifestation.UNKNOWN_PROPERTY");
    }

    @Test
    void load_entityHasIntegerProperty_setsValue() {
        var entity = Json.load("{\"age\": 44, \"agePrimitive\": 22}", SampleTypedEntity.class);

        assertThat(entity.age()).isEqualTo(44);
        assertThat(entity.agePrimitive()).isEqualTo(22);
    }

    record FloatEntity(Float age, float agePrimitive) {

    }

    @Test
    void load_entityIsMissingFloatProperty_defaultsToZero() {
        var entity = Json.load(EMPTY, FloatEntity.class);
        assertThat(entity.age()).isZero();
        assertThat(entity.agePrimitive()).isZero();
    }

    @Test
    void load_entityhasFloatProperty_deserializesFloat() {
        var entity = Json.load("{\"age\":2.1,\"agePrimitive\":\"2.3\"}", FloatEntity.class);
        assertThat(entity.age()).isEqualTo(2.1f);
        assertThat(entity.agePrimitive()).isEqualTo(2.3f);
    }

    record DoubleEntity(Double age, double agePrimitive) {

    }

    @Test
    void load_entityIsMissingDoubleProperty_defaultsToZero() {
        var entity = Json.load(EMPTY, DoubleEntity.class);
        assertThat(entity.age()).isZero();
        assertThat(entity.agePrimitive()).isZero();
    }

    @Test
    void load_entityhasDoubleProperty_deserializesDouble() {
        var entity = Json.load("{\"age\":2.1,\"agePrimitive\":\"2.3\"}", DoubleEntity.class);
        assertThat(entity.age()).isEqualTo(2.1);
        assertThat(entity.agePrimitive()).isEqualTo(2.3);
    }

    @Test
    void load_jsonContainsEmptySpaceAndLineFeeds_setsValues() {
        var entity = Json.load("""
            {
                   \"enumeration\":     FINAL,
            
            
                \"age\"   :   44
            
            }
            """, SampleTypedEntity.class);

        assertThat(entity.age()).isEqualTo(44);
        assertThat(entity.enumeration()).isEqualTo(TypeManifestation.FINAL);
    }

    record EntityWithChild(String name, EntityWithChild child) {

    }

    @Test
    void load_jsonContainsAnotherEntity_deserializesEntity() {
        var entity = Json.load("{ \"name\": \"root\", \"child\": { \"name\": \"child\", \"child\": { \"name\": \"l2_child\" }} }", EntityWithChild.class);

        assertThat(entity.name()).isEqualTo("root");
        assertThat(entity.child().name()).isEqualTo("child");
        assertThat(entity.child().child().name()).isEqualTo("l2_child");
        assertThat(entity.child().child().child()).isNull();
    }

    record EntityWithList(List<Integer> numbers) {

    }

    @Test
    void load_jsonIsMissingList_isEmpty() {
        var entity = Json.load("{}", EntityWithList.class);

        assertThat(entity.numbers()).isEmpty();
    }

    @Test
    void load_jsonContainsEmptyList_isEmpty() {
        var entity = Json.load("{ \"numbers\" : [] }", EntityWithList.class);

        assertThat(entity.numbers()).isEmpty();
    }

    @Test
    void load_jsonContainsList_isDeserialized() {
        var entity = Json.load("{ \"numbers\" : [1, 4, 3] }", EntityWithList.class);

        assertThat(entity.numbers()).containsExactly(1, 4, 3);
    }

    record EntityWithListEntity(List<SampleEntity> entries) {

    }

    //TODO  add test for stacked arrays
    // TODO add test for stacked values
    @Test
    void load_entityWithinList_deserializesEntityList() {
        var entity = Json.load("""
            {
                "entries": [
                {
                    "name": "Bas",
                    "city": "Cologne"
                },
                {
                    "name": "Debo",
                    "city": "Cologne"
                },
                ]
            }
            """, EntityWithListEntity.class);

        assertThat(entity.entries()).containsExactly(
            new SampleEntity("Bas", "Cologne"),
            new SampleEntity("Debo", "Cologne"));
    }

    record ReservedKeywordsEntity(String class_, String interface_) {

    }
    //TODO add to javadoc of Json.load
    @Test
    void load_entityNameEndsWithUnderscore_deserializesInto() {
        var entity = Json.load("{\"class\":\"reserved1\", \"interface\":\"reserved2\"}", ReservedKeywordsEntity.class);

        assertThat(entity.class_()).isEqualTo("reserved1");
        assertThat(entity.interface_()).isEqualTo("reserved2");
    }
}
