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
    void newInstance_entitiesIsNull_isEmpty() {
        Properties newInstance = new Properties(null);
        assertThat(newInstance.all()).isEmpty();
    }

    @Test
    void tryGetString_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.tryGetString("unknown")).isEmpty();
    }

    @Test
    void tryGetInt_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.tryGetInt("unknown")).isEmpty();
    }

    @Test
    void tryGetDouble_propertyMissing_returnsEmptyOptional() {
        assertThat(properties.tryGetDouble("unknown")).isEmpty();
    }

    @Test
    void tryGetString_propertyExists_returnsValue() {
        assertThat(properties.tryGetString("material")).isEqualTo(Optional.of("ice"));
    }

    @Test
    void tryGetInt_propertyExists_returnsValue() {
        assertThat(properties.tryGetInt("referenceId")).isEqualTo(Optional.of(125));
    }

    @Test
    void tryGetDouble_propertyExists_returnsValue() {
        assertThat(properties.tryGetDouble("length")).isEqualTo(Optional.of(15.5));
    }

    @Test
    void tryGetDouble_propertyNotANumber_throwsException() {
        assertThatThrownBy(() -> properties.tryGetDouble("material"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property material is not a number: ice");
    }

    @Test
    void tryGetInt_propertyNotANumber_throwsException() {
        assertThatThrownBy(() -> properties.tryGetInt("material"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property material is not a number: ice");
    }

    @Test
    void tryGetBoolean_valueSetToFalse_returnsOptionalOfFalse() {
        assertThat(properties.tryGetBoolean("unchecked")).isEqualTo(Optional.of(false));
    }

    @Test
    void getBoolean_valueSetToTrue_returnsTrue() {
        assertThat(properties.getBoolean("checked")).isTrue();
    }

    private PropertyEntity createPropertyEntity(String name, String value) {
        return new PropertyEntity(name, "type", value);
    }
}
