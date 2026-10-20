package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
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
        simulation.addDensity(-4, -1, 50, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsObstacle_densityIsUnchanged() {
        simulation.setObstacle(4, 4);
        simulation.addDensity(4, 4, 50, Color.RED);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void addDensity_cellIsFree_densityIsUpdatedToLimit() {
        simulation.addDensity(4, 4, 2, Color.RED);

        final var state = simulation.densityData();
        assertThat(state.red(4, 4)).isEqualTo(2.0);
        assertThat(state.green(4, 4)).isZero();
        assertThat(state.blue(4, 4)).isZero();
    }

    @Test
    void step_noDensity_densityIsStillZero() {
        simulation.step(0.01, 0.0000000004, 0.000001, 2);

        assertAllCellsHaveZeroDensity();
    }

    @Test
    void step_densityIsSet_diffusesToNeighbours() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell.x(), cell.y(), 128, Color.ORANGE);


        var state = simulation.densityData();
        assertThat(state.red(cell.x(), 4)).isEqualTo(128.0, offset(0.01));
        assertThat(state.green(cell.x(), cell.y())).isEqualTo(82.8, offset(0.1));
        assertThat(state.blue(cell.x(), cell.y())).isZero();

        for (var neighbour : Set.of(cell.top(), cell.bottom(), cell.left(), cell.right())) {
            assertThat(state.red(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.green(neighbour.x(), neighbour.y())).isEqualTo(0, offset(0.01));
            assertThat(state.blue(neighbour.x(), neighbour.y())).isZero();
            assertThat(state.alpha(neighbour.x(), neighbour.y())).isZero();
        }

        assertTotalRedDensity(128.0);
    }

    @Test
    void step_gridIsSplitIntoTwoHalfs_colorDoesNotDiffuseOverObstacles() {
        for (int y = 0; y < 32; y++) {
            simulation.setObstacle(20, y);
        }
        simulation.addDensity(19, 16, 100, Color.RED);

        for (int i = 0; i < 20; i++) {
            simulation.step(0.1, 0.0004, 0.0003, 3);
        }

        DensityInfo state = simulation.densityData();
        assertThat(state.red(18, 16)).isNotZero();
        assertThat(state.red(19, 16)).isNotZero();
        assertThat(state.red(19, 15)).isNotZero();
        assertThat(state.red(19, 17)).isNotZero();
        assertThat(state.red(20, 16)).isZero();
        assertThat(state.red(21, 16)).isZero();
        assertThat(state.red(22, 16)).isZero();
    }

    @Test
    void fade_noDensity_leavesDensityUnchanged() {
        simulation.fade(20);

        assertTotalRedDensity(0);
    }

    @Test
    void fade_cellHasDensity_reducesDensity() {
        simulation.addDensity(4, 9, 512, Color.rgb(12, 31, 21));

        simulation.fade(2);

        assertThat(simulation.densityData().red(4, 9)).isEqualTo(22.0, offset(0.3));
        assertThat(simulation.densityData().green(4, 9)).isEqualTo(60.0, offset(0.3));
        assertThat(simulation.densityData().blue(4, 9)).isEqualTo(40.0, offset(0.3));
    }

    @Test
    void hasDensity_noDensity_isFalse() {
        assertThat(simulation.hasDensity()).isFalse();
    }

    @Test
    void hasDensity_redPresent_isTrue() {
        simulation.addDensity(4, 4, 1, Color.rgb(1, 0, 0, Percent.zero()));

        assertThat(simulation.hasDensity()).isTrue();
    }

    @Test
    void hasDensity_greenPresent_isTrue() {
        simulation.addDensity(4, 4, 1, Color.rgb(0, 1, 0, Percent.zero()));

        assertThat(simulation.hasDensity()).isTrue();
    }

    @Test
    void hasDensity_bluePresent_isTrue() {
        simulation.addDensity(4, 4, 1, Color.rgb(0, 0, 1, Percent.zero()));

        assertThat(simulation.hasDensity()).isTrue();
    }

    @Test
    void hasDensity_alphaPresent_isTrue() {
        simulation.addDensity(4, 4, 1, Color.rgb(0, 0, 0, Percent.of(0.1)));

        assertThat(simulation.hasDensity()).isTrue();
    }

    @Test
    void advanceVelocity_noVelocity_smoothlyAdjustsVelocity() {
        simulation.advanceVelocity(4, 9, Vector.of(10, 20), 0.5);

        assertThat(simulation.velocityAt(4, 9)).isEqualTo(Vector.of(0.5, 0.5));

        simulation.advanceVelocity(4, 9, Vector.of(10, 20), 50);

        assertThat(simulation.velocityAt(4, 9)).isEqualTo(Vector.of(10, 20));
    }

    @Test
    void densityAt_fullBlownRed_isRed() {
        simulation.addDensity(4, 9, 2.0, Color.RED);

        assertThat(simulation.densityAt(4, 9)).isEqualTo(Color.RED);
    }

    @Test
    void densityAt_noDensity_isTransparent() {
        assertThat(simulation.densityAt(4, 9)).isEqualTo(Color.TRANSPARENT);
    }

    @Test
    void loadCells_sameSize_copiesCellsWithOffset() {
        var oldSimulation = new FluidSimulation(16);
        oldSimulation.addDensity(1, 1, 1, Color.RED);
        oldSimulation.addDensity(10, 10, 1, Color.BLUE);

        var newSimulation = new FluidSimulation(16);

        newSimulation.loadCells(oldSimulation, 6, 1);

        assertThat(newSimulation.densityAt(1, 1)).isEqualTo(Color.TRANSPARENT);
        assertThat(newSimulation.densityAt(4, 9)).isEqualTo(Color.BLUE);
    }

    @Test
    void loadCells_bigger_copiesCellsWithOffset() {
        var oldSimulation = new FluidSimulation(16);
        oldSimulation.addDensity(1, 1, 1, Color.RED);
        oldSimulation.addDensity(10, 10, 1, Color.BLUE);

        var newSimulation = new FluidSimulation(20);

        newSimulation.loadCells(oldSimulation, 6, 1);

        assertThat(newSimulation.densityAt(1, 1)).isEqualTo(Color.TRANSPARENT);
        assertThat(newSimulation.densityAt(4, 9)).isEqualTo(Color.BLUE);
    }


    private void assertTotalRedDensity(double expectedDensity) {
        final var state = simulation.densityData();
        var sum = 0.0;
        for (var c : Size.square(32).all()) {
            sum += state.red(c.x(), c.y());

        }
        assertThat(sum).isEqualTo(expectedDensity, offset(0.01));
    }

    private void assertAllCellsHaveZeroDensity() {
        final var state = simulation.densityData();
        for (var cell : Size.square(32).all()) {
            assertThat(state.red(cell.x(), cell.y())).isZero();
            assertThat(state.green(cell.x(), cell.y())).isZero();
            assertThat(state.blue(cell.x(), cell.y())).isZero();
            assertThat(state.alpha(cell.x(), cell.y())).isZero();
        }
    }
}
