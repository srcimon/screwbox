package de.suzufa.screwbox.examples.lights;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;

public class LightsExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Lights Example");

        engine.entities().add(new EntitySystem() {

            @Override
            public void update(Engine engine) {
                engine.graphics().window().fillWith(Color.DARK_BLUE);
            }
        });
        engine.start();
    }
}
