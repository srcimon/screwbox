package dev.screwbox.tiled;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.navigation.Borders;
import dev.screwbox.tiled.internal.PropertyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class PropertiesTest {

    private Properties properties;

    @BeforeEach
    void beforeEach() {
        List<PropertyEntity> propertyEntities = List.of(
            createPropertyEntity("color", "#ffce9f9e"),
            createPropertyEntity("colorWithOpacity", "#b2ce9f9e"),
            createPropertyEntity("material", "ice"),
            createPropertyEntity("length", "15.5"),
            createPropertyEntity("border", "TOp"),
            createPropertyEntity("borderinvalid", "unknown"),
            createPropertyEntity("referenceId", "125"),
            createPropertyEntity("unchecked", "falsE"),
            createPropertyEntity("checked", "true"));

        properties = new Properties(propertyEntities);
    }

    @Test
    void tryGetEnum_propertyFoundAndNameMatches_returnsValue() {
        assertThat(properties.tryGetEnum("border", Borders.class)).contains(Borders.TOP);
    }

    @Test
    void tryGetEnum_propertyFoundButNoMatch_isEmpty() {
        assertThat(properties.tryGetEnum("borderinvalid", Borders.class)).isEmpty();
    }

    @Test
    void tryGetEnum_propertyNotFound_isEmpty() {
        assertThat(properties.tryGetEnum("unknown", Borders.class)).isEmpty();
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

    @Test
    void tryGetColor_valueIsColor_returnsOptionalOfColor() {
        assertThat(properties.tryGetColor("color")).contains(Color.rgb(206, 159, 158));
    }

    @Test
    void tryGetColor_propertyDoesNotExist_isEmpty() {
        assertThat(properties.tryGetColor("unknown")).isEmpty();
    }

    @Test
    void getColor_valueIsColor_returnsColor() {
        assertThat(properties.getColor("color")).isEqualTo(Color.rgb(206, 159, 158));
    }

    @Test
    void getColor_valueIsColorWithOpacity_returnsColor() {
        Color color = properties.getColor("colorWithOpacity");
        assertThat(color.r()).isEqualTo(206);
        assertThat(color.g()).isEqualTo(159);
        assertThat(color.b()).isEqualTo(158);
        assertThat(color.opacity().value()).isEqualTo(0.69, offset(0.01));
    }

    private PropertyEntity createPropertyEntity(final String name, final String value) {
        return new PropertyEntity(name, "type", value);
    }
}
