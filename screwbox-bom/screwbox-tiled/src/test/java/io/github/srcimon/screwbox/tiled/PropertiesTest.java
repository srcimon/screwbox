package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.tiled.internal.PropertyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertiesTest {

    private Properties properties;

    @BeforeEach
    void beforeEach() {
        List<PropertyEntity> propertyEntities = List.of(
                createPropertyEntity("material", "ice"),
                createPropertyEntity("length", "15.5"),
                createPropertyEntity("referenceId", "125"),
                createPropertyEntity("unchecked", "falsE"),
                createPropertyEntity("checked", "true"));

        properties = new Properties(propertyEntities);
    }

    @Test
    void get_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.get("unknown")).isEmpty();
    }

    @Test
    void getInt_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.getInt("unknown")).isEmpty();
    }

    @Test
    void getDouble_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.getDouble("unknown")).isEmpty();
    }

    @Test
    void get_propertyExists_returnsValue() {
        assertThat(properties.get("material")).isEqualTo(Optional.of("ice"));
    }

    @Test
    void getInt_propertyExists_returnsValue() {
        assertThat(properties.getInt("referenceId")).isEqualTo(Optional.of(125));
    }

    @Test
    void getDouble_propertyExists_returnsValue() {
        assertThat(properties.getDouble("length")).isEqualTo(Optional.of(15.5));
    }

    @Test
    void getDouble_propertyNotANumber_throwsException() {
        assertThatThrownBy(() -> properties.getDouble("material"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property material is not a number: ice");
    }

    @Test
    void getInt_propertyNotANumber_throwsException() {
        assertThatThrownBy(() -> properties.getInt("material"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property material is not a number: ice");
    }

    @Test
    void getBoolean_valueSetToFalse_returnsOptionalOfFalse() {
        assertThat(properties.getBoolean("unchecked")).isEqualTo(Optional.of(false));
    }

    @Test
    void forceBoolean_valueSetToTrue_returnsTrue() {
        assertThat(properties.forceBoolean("checked")).isTrue();
    }

    private PropertyEntity createPropertyEntity(String name, String value) {
        return new PropertyEntity(name, "type", value);
    }
}
