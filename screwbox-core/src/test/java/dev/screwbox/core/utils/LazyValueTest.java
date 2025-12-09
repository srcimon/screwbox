package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class LazyValueTest {

    @Test
    void value_multipleCalls_onlyOneCallToSupplier(@Mock Supplier<String> valueSupplierMock) {
        when(valueSupplierMock.get()).thenReturn("value");
        var wrapper = new LazyValue<>(valueSupplierMock);

        assertThat(wrapper.value()).isEqualTo("value");
        assertThat(wrapper.value()).isEqualTo("value");
        verify(valueSupplierMock, atMostOnce()).get();
    }

    @Test
    void value_supplierReturnsValue_returnsValueFromSupplier() {
        var wrapper = new LazyValue<>(() -> 4);
        assertThat(wrapper.value()).isEqualTo(4);
    }

    @Test
    void value_supplierReturnsNull_returnsNull() {
        var wrapper = new LazyValue<String>(() -> null);
        assertThat(wrapper.value()).isNull();
    }
}
