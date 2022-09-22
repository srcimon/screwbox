package de.suzufa.screwbox.examples.gameoflife;

import static de.suzufa.screwbox.core.graphics.PredefinedCursor.DEFAULT;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;
import de.suzufa.screwbox.examples.gameoflife.systems.CameraControlSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridRenderSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridUpdateSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridInteractionSystem;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life Example");

        engine.entities()
                .add(new Entity().add(new GridComponent()))
                .add(new GridUpdateSystem())
                .add(new GridRenderSystem())
                .add(new GridInteractionSystem())
                .add(new LogFpsSystem())
                .add(new CameraControlSystem());

        engine.graphics().window().setCursor(DEFAULT);
        engine.start();
    }
}
