package dev.screwbox.playground.simulate;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.navigation.Grid;

public class GasSimulationComponent implements Component {

    public final int cellSize;

    public Grid<GasCellState> state;

    public GasSimulationComponent(final int cellSize) {
        this.cellSize = cellSize;
    }
}
