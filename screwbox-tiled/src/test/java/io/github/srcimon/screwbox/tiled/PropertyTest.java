package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.tiled.internal.PropertyEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertyTest {

    @Test
    void name_returnsName() {
        var property = createNumberProperty();
        assertThat(property.name()).isEqualTo("hue");
    }

    @Test
    void get_returnsValue() {
        var property = createNumberProperty();
        assertThat(property.get()).isEqualTo("20");
    }

    @Test
    void getInt_isConvertible_returnsIntValue() {
        var property = createNumberProperty();
        assertThat(property.getInt()).isEqualTo(20);
    }

    @Test
    void getDouble_isConvertible_returnsDoubleValue() {
        var property = createNumberProperty();
        assertThat(property.getDouble()).isEqualTo(20.0);
    }

    @Test
    void hasValue_valueIsEmpty_returnsFalse() {
        var property = new Property(new PropertyEntity("isOn", "type", ""));
        assertThat(property.hasValue()).isFalse();
    }

    @Test
    void hasValue_isNotEmpty_returnsTrue() {
        var property = createNumberProperty();
        assertThat(property.hasValue()).isTrue();
    }

    @Test
    void getBoolean_isTrue_returnsTrue() {
        var property = createBooleanProperty();
        assertThat(property.getBoolean()).isTrue();
    }

    @Test
    void getDouble_isNotConvertible_throwsException() {
        var property = createBooleanProperty();
        assertThatThrownBy(property::getDouble)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property isOn is not a number: true");
    }

    @Test
    void getInt_isNotConvertible_throwsException() {
        var property = createBooleanProperty();
        assertThatThrownBy(property::getInt)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("property isOn is not a number: true");
    }

    private Property createNumberProperty() {
        var property = new PropertyEntity("hue", "int", "20");
        return new Property(property);
    }

    private Property createBooleanProperty() {
        var property = new PropertyEntity("isOn", "type", "true");
        return new Property(property);
    }
}
