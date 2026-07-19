package dev.screwbox.playground.simulate;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;
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
                    var c = Offset.at(x, y);
                    simulation.state.get(x, y).density = simulation.state.get(c).density
                                                         + simulation.state.get(c.top()).density * 0.001
                                                         - simulation.state.get(c.bottom()).density * 0.001
                                                         + simulation.state.get(c.left()).density * 0.001
                                                         - simulation.state.get(c.right()).density * 0.001
                                                         + simulation.state.get(c.right()).density * 0.001
                                                         - simulation.state.get(c.left()).density * 0.001
                                                         + simulation.state.get(c.top()).density * 0.001
                                                         - simulation.state.get(c.right()).density * 0.001;

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
