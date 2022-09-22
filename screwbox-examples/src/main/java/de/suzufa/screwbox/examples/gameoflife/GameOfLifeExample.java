package de.suzufa.screwbox.examples.gameoflife;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.MouseCameraSystem;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine(); // TODO: createEngine(name)
                                                 // createEngine(info.name/creator/version,site)
        engine.entities()
                .add(new GridSystem())
                .add(new LogFpsSystem())
                .add(new MouseCameraSystem());
        engine.graphics().window().setTitle("Game Of Life example");
        engine.start();
    }
}
