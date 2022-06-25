package de.suzufa.screwbox.examples.helloworld.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;

public class PrintHelloWorldSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Font font = new Font("Arial", 36);
        Offset mousePosition = engine.mouse().position();
        engine.graphics().window().drawTextCentered(mousePosition, "Hello World", font);
    }

}
