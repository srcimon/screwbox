package dev.screwbox.core.environment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void drawOrder_higherExecutionOrder_isGreater() {
        assertThat(Order.PRESENTATION_LIGHT.drawOrder()).isGreaterThan(Order.PRESENTATION_WORLD.drawOrder());
    }

    @Test
    void surpassDrawOrder_sameOrder_isHigherThanDrawOrder() {
        assertThat(Order.PRESENTATION_LIGHT.surpassDrawOrder()).isGreaterThan(Order.PRESENTATION_LIGHT.drawOrder());
    }
}
