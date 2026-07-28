package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

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

    @Test
    void step_noDensity_densityIsStillZero() {
        simulation.step(0.01, 0.0000000004, 0.000001, 2);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void step_densityIsSet_diffusesToNeighbours() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell, 50, 50, Color.ORANGE);


        var state = simulation.state();
        assertThat(state.densityRed(cell.x(), cell.y())).isEqualTo(49.99, offset(0.01));
        assertThat(state.densityGreen(cell.x(), cell.y())).isEqualTo(49.99, offset(0.01));
        assertThat(state.densityBlue(cell.x(), cell.y())).isZero();

        for (var neighbour : Set.of(cell.top(), cell.bottom(), cell.left(), cell.right())) {
            assertThat(state.densityRed(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.densityGreen(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.densityBlue(neighbour.x(), neighbour.y())).isZero();
        }

        assertTotalRedDensity(50.0);
    }

    private void assertTotalRedDensity(double expectedDensity) {
        final var state = simulation.state();
        var sum = 0.0;
        for (var c : Size.square(32).all()) {
            sum += state.densityRed(c.x(), c.y());

        }
        assertThat(sum).isEqualTo(expectedDensity, offset(0.01));
    }

    private void assertAllCellsHaveZeroDensity() {
        final var state = simulation.state();
        for (var cell : Size.square(32).all()) {
            assertThat(state.densityRed(cell.x(), cell.y())).isZero();
            assertThat(state.densityGreen(cell.x(), cell.y())).isZero();
            assertThat(state.densityBlue(cell.x(), cell.y())).isZero();
        }
    }
}
