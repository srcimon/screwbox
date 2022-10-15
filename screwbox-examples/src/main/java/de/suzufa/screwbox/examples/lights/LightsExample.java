package de.suzufa.screwbox.examples.lights;

import java.util.ArrayList;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Lightmap;
import de.suzufa.screwbox.core.graphics.Offset;

public class LightsExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Lights Example");

        engine.entities().add(new EntitySystem() {

            @Override
            public void update(Engine engine) {
                engine.graphics().window().fillWith(Color.DARK_BLUE);

                var lightmap = new Lightmap(engine.graphics().window().size());
                ArrayList<Offset> area = new ArrayList<>();
                area.add(Offset.at(20, 20));
                area.add(Offset.at(420, 30));
                area.add(Offset.at(420, 300));
                area.add(Offset.at(10, 300));
                lightmap.addPointLight(Offset.at(140, 200), 180, area);
                engine.graphics().window().drawSprite(lightmap.createImage(), Offset.origin());
            }
        });
        engine.start();
    }
}
