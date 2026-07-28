package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FluidSimulationTest {

    FluidSimulation simulation;

    @BeforeEach
    void setUp() {
        simulation = new FluidSimulation(32);
    }

    @Test
    void newInstance_32cells_initializes32x32EmptyCells() {
        assertThat(simulation.resolution()).isEqualTo(32);
        assertAllCellsHaveZeroDensity();
    }

    private void assertAllCellsHaveZeroDensity() {
        final var state = simulation.state();
        for (var cell : Size.square(32).all()) {
            assertThat(state.densityRed(cell.x(), cell.y())).isZero();
            assertThat(state.densityGreen(cell.x(), cell.y())).isZero();
            assertThat(state.densityBlue(cell.x(), cell.y())).isZero();
        }
    }

    @Test
    void addDensity_outOfBounds_densityIsUnchanged() {
        simulation.addDensity(Offset.at(-4, -1), 50, 100, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsObstacle_densityIsUnchanged() {
        simulation.setObstacle(4, 4);
        simulation.addDensity(Offset.at(4, 4), 50, 100, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsFree_densityIsUpdatedToLimit() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell, 120, 100, Color.RED);

        final var state = simulation.state();
        assertThat(state.densityRed(cell.x(), cell.y())).isEqualTo(100);
        assertThat(state.densityGreen(cell.x(), cell.y())).isZero();
        assertThat(state.densityBlue(cell.x(), cell.y())).isZero();
    }
}
