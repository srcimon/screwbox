package de.suzufa.screwbox.examples.gameoflife;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;
import de.suzufa.screwbox.examples.gameoflife.systems.CameraControlSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridInteractionSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridRenderSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.GridUpdateSystem;
import de.suzufa.screwbox.examples.gameoflife.systems.PauseSystem;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life Example");

        engine.entities()
                .add(new Entity().add(new GridComponent()))
                .add(new GridUpdateSystem())
                .add(new GridRenderSystem())
                .add(new GridInteractionSystem())
                .add(new PauseSystem(Key.SPACE))
                .add(new QuitOnKeyPressSystem(Key.ESCAPE))
                .add(new LogFpsSystem())
                .add(new CameraControlSystem());

        engine.graphics().configuration().setUseAntialiasing(true);

        engine.graphics().updateCameraZoom(6);
        engine.start();
    }
}
