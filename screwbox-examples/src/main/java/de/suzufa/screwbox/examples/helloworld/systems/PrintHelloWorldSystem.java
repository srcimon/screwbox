package de.suzufa.screwbox.examples.helloworld.systems;

import static de.suzufa.screwbox.core.graphics.Color.WHITE;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;

public class PrintHelloWorldSystem implements EntitySystem {

    private static final Pixelfont FONT = Pixelfont.defaultFont(WHITE);

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().position();
        engine.graphics().window().drawTextCentered(mousePosition, "HELLO WORLD!", FONT, 4);
    }

}
