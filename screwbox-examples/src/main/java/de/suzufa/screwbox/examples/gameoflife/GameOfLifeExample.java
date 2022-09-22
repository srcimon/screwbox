package de.suzufa.screwbox.examples.gameoflife;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.examples.gameoflife.systems.GridSystem;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine(); // TODO: createEngine(name)
                                                 // createEngine(info.name/creator/version,site)
        engine.entities().add(new GridSystem());
        engine.start();
    }
}
