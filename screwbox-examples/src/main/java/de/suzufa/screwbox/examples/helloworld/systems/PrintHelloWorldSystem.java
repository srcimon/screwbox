package de.suzufa.screwbox.examples.helloworld.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;

public class PrintHelloWorldSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().position();
        engine.graphics().window().drawTextCentered(mousePosition, "HELLO WORLD!", Pixelfont.defaultWhite(),
                Percentage.max(), 4);
    }

}
