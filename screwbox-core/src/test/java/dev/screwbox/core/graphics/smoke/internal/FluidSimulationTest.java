package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FluidSimulationTest {

    @Test
    void newInstance_32cells_initializes32x32EmptyCells() {
        var simulation = new FluidSimulation(32);

        assertThat(simulation.size()).isEqualTo(32);

        var state = simulation.state();
        for (var cell : Size.square(32).all()) {
            assertThat(state.densityRed(cell.x(), cell.y())).isZero();
            assertThat(state.densityGreen(cell.x(), cell.y())).isZero();
            assertThat(state.densityBlue(cell.x(), cell.y())).isZero();
        }
    }
}
