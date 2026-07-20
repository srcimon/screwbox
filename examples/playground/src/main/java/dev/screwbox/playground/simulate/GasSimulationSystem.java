package dev.screwbox.playground.simulate;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.utils.PerlinNoise;

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
                simulation.state = Grid.createByCellSize(Size.square(simulation.cellSize), gas.bounds(), GasCellState.class);
                simulation.state.fill(GasCellState::new);
                initRandomly(simulation, random);
            }
            for (int y = 0; y < simulation.state.size().height(); y++) {
                for (int x = 0; x < simulation.state.size().width(); x++) {
                    var c = Offset.at(x, y);
                    simulation.state.get(c).density = simulation.state.get(c).density + 0.1 * PerlinNoise.generatePerlinNoise(1230L, (x + engine.loop().frameNumber()) / 40.0, (y + engine.loop().frameNumber()) / 40.0);
//                    simulation.state.get(x, y).density = simulation.state.get(c).density
//                                                         + simulation.state.get(c.top()).density * 0.001
//                                                         - simulation.state.get(c.bottom()).density * 0.001
//                                                         + simulation.state.get(c.left()).density * 0.001
//                                                         - simulation.state.get(c.right()).density * 0.001
//                                                         + simulation.state.get(c.right()).density * 0.001
//                                                         - simulation.state.get(c.left()).density * 0.001
//                                                         + simulation.state.get(c.top()).density * 0.001
//                                                         - simulation.state.get(c.right()).density * 0.001;

                }
            }
        }
    }

    private static void initRandomly(GasSimulationComponent simulation, Random random) {
        for (int y = 0; y < simulation.state.size().height(); y++) {
            for (int x = 0; x < simulation.state.size().width(); x++) {
                simulation.state.get(x, y).density = random.nextDouble();
            }
        }
    }
}
