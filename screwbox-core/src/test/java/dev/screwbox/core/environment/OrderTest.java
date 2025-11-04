package dev.screwbox.core.environment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void drawOrder_higherExecutionOrder_isGreater() {
        assertThat(Order.PRESENTATION_LIGHT.drawOrder()).isGreaterThan(Order.PRESENTATION_WORLD.drawOrder());
    }

    @Test
    void mixinDrawOrder_valueOne_returnsDrawOrderWithinLightRenderPhase() {
        assertThat(Order.PRESENTATION_LIGHT.mixinDrawOrder(1)).isEqualTo(9_000_001);
    }
}
