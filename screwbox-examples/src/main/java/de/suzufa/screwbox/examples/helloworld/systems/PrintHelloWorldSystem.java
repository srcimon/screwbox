package de.suzufa.screwbox.examples.helloworld.systems;

import static de.suzufa.screwbox.core.graphics.Color.WHITE;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;

public class PrintHelloWorldSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().position();
        engine.graphics().window().drawTextCentered(mousePosition, "HELLO WORLD!", Pixelfont.defaultFont(WHITE), 4);
    }

}
