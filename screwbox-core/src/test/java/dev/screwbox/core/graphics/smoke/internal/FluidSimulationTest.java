package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
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

    @Test
    void step_gridIsSplitIntoTwoHalfs_colorDoesNotDiffuseOverObstacles() {
        for (int y = 0; y < 32; y++) {
            simulation.setObstacle(20, y);
        }
        simulation.addDensity(Offset.at(19, 16), 100, 100, Color.RED);

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
    void step_movingSmokePresent_animatesSmoke() {
        Offset redCell = Offset.at(8, 4);
        simulation.addDensity(redCell, 20, 20, Color.RED);
        simulation.addVelocity(redCell, Vector.x(10), 100);

        Offset greenCell = Offset.at(2, 2);
        simulation.addDensity(greenCell, 30, 30, Color.GREEN);
        simulation.addVelocity(greenCell, Vector.$(5, 3), 100);

        Offset blueCell = Offset.at(25, 20);
        simulation.addDensity(blueCell, 40, 40, Color.BLUE);
        simulation.addVelocity(blueCell, Vector.$(-20, -10), 100);

        for (int i = 0; i < 10; i++) {
            simulation.step(0.1, 0.0004, 0.0003, 3);
        }

        verifyIsSameImage(renderFluid(), "fluidsimulation/step_movingSmokePresent_animatesSmoke_1.png");

        for (int i = 0; i < 30; i++) {
            simulation.step(0.1, 0.000004, 0.00003, 3);
        }

        verifyIsSameImage(renderFluid(), "fluidsimulation/step_movingSmokePresent_animatesSmoke_2.png");

    }

    private Image renderFluid() {
        return new SmokeRenderer().createImage(0, 1, Percent.max(), simulation.state(), new ScreenBounds(0, 0, 32, 32)).singleImage();
    }

    @Test
    void fade_noDensity_leavesDensityUnchanged() {
        simulation.fade(20);

        assertTotalRedDensity(0);
    }

    @Test
    void fade_cellHasDensity_reducesDensity() {
        Offset cell = Offset.at(4, 4);
        simulation.addDensity(cell, 2, 1000, Color.rgb(12, 31, 21));

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
        }
    }
}
