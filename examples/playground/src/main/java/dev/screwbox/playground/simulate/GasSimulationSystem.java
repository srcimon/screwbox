package dev.screwbox.playground.simulate;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.navigation.Grid;

import java.util.Random;

import static java.util.Objects.isNull;

public class GasSimulationSystem implements EntitySystem {

    private static final Archetype GASSES = Archetype.ofSpacial(GasSimulationComponent.class);

    @Override
    public void update(Engine engine) {
        Random random = new Random();

        for (final var gas : engine.environment().fetchAll(GASSES)) {
            var simulation = gas.get(GasSimulationComponent.class);
            if (isNull(simulation.state)) {
                simulation.state = new Grid<>(gas.bounds(), simulation.cellSize, GasCellState.class);
                simulation.state.enableAutoPadding(new GasCellState());
                simulation.state.fill(GasCellState::new);
                initRandomly(simulation, random);
            }
            for (int y = 0; y < simulation.state.height(); y++) {
                for (int x = 0; x < simulation.state.width(); x++) {
                    simulation.state.get(x, y).density = simulation.state.get(x, y).density
                                                         + simulation.state.get(x + 1, y).density * 0.01
                                                         - simulation.state.get(x + 1, y - 1).density * 0.01
                                                         - simulation.state.get(x - 1, y).density * 0.01
                                                         + simulation.state.get(x, y + 1).density * 0.02;
                }
            }
        }
    }

    private static void initRandomly(GasSimulationComponent simulation, Random random) {
        for (int y = 0; y < simulation.state.height(); y++) {
            for (int x = 0; x < simulation.state.width(); x++) {
                simulation.state.get(x, y).density = random.nextDouble();
            }
        }
    }
}
