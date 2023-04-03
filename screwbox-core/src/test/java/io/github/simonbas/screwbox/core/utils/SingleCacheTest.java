package io.github.simonbas.screwbox.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SingleCacheTest {

    SingleCache<String> textCache;

    @BeforeEach
    void setUp() {
        textCache = new SingleCache<>();
    }

    @Test
    void getOrElse_supplierNull_throwsException() {
        assertThatThrownBy(() -> textCache.getOrElse(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("value supplier must not be null");
    }

    @Test
    void getOrElse_supplierPresent_returnsValueFromSupplier() {
        String result = textCache.getOrElse(() -> "value");

        assertThat(result).isEqualTo("value");
    }

    @Test
    void getOrElse_valueAlreadyCached_returnsValue() {
        textCache.getOrElse(() -> "initial value");

        var supplierMock = mock(Supplier.class);
        String result = textCache.getOrElse(supplierMock);

        assertThat(result).isEqualTo("initial value");
        verify(supplierMock, never()).get();
    }
}
