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
        simulation.addDensity(Offset.at(-4, -1), 50, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsObstacle_densityIsUnchanged() {
        simulation.setObstacle(4, 4);
        simulation.addDensity(Offset.at(4, 4), 50, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsFree_densityIsUpdatedToLimit() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell, 2, Color.RED);

        final var state = simulation.state();
        assertThat(state.densityRed(cell.x(), cell.y())).isEqualTo(510);
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
        simulation.addDensity(cell, 0.5, Color.ORANGE);


        var state = simulation.state();
        assertThat(state.densityRed(cell.x(), cell.y())).isEqualTo(127.5, offset(0.01));
        assertThat(state.densityGreen(cell.x(), cell.y())).isEqualTo(82.5, offset(0.01));
        assertThat(state.densityBlue(cell.x(), cell.y())).isZero();

        for (var neighbour : Set.of(cell.top(), cell.bottom(), cell.left(), cell.right())) {
            assertThat(state.densityRed(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.densityGreen(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.densityBlue(neighbour.x(), neighbour.y())).isZero();
            assertThat(state.densityAlpha(neighbour.x(), neighbour.y())).isZero();
        }

        assertTotalRedDensity(127.5);
    }

    @Test
    void step_gridIsSplitIntoTwoHalfs_colorDoesNotDiffuseOverObstacles() {
        for (int y = 0; y < 32; y++) {
            simulation.setObstacle(20, y);
        }
        simulation.addDensity(Offset.at(19, 16), 100, Color.RED);

        for (int i = 0; i < 20; i++) {
            simulation.step(0.1, 0.0004, 0.0003, 3);
        }

        FluidSimulationState state = simulation.state();
        assertThat(state.densityRed(18, 16)).isNotZero();
        assertThat(state.densityRed(19, 16)).isNotZero();
        assertThat(state.densityRed(19, 15)).isNotZero();
        assertThat(state.densityRed(19, 17)).isNotZero();
        assertThat(state.densityRed(20, 16)).isZero();
        assertThat(state.densityRed(21, 16)).isZero();
        assertThat(state.densityRed(22, 16)).isZero();
    }

    @Test
    void fade_noDensity_leavesDensityUnchanged() {
        simulation.fade(20);

        assertTotalRedDensity(0);
    }

    @Test
    void fade_cellHasDensity_reducesDensity() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell, 2, Color.rgb(12, 31, 21));

        simulation.fade(2);

        assertThat(simulation.state().densityRed(cell.x(), cell.y())).isEqualTo(22.0);
        assertThat(simulation.state().densityGreen(cell.x(), cell.y())).isEqualTo(60.0);
        assertThat(simulation.state().densityBlue(cell.x(), cell.y())).isEqualTo(40.0);
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
            assertThat(state.densityAlpha(cell.x(), cell.y())).isZero();
        }
    }
}
