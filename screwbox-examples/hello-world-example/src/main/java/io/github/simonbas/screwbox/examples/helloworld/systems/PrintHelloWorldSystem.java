package io.github.simonbas.screwbox.examples.helloworld.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Pixelfont;

import static io.github.simonbas.screwbox.core.graphics.Color.WHITE;

public class PrintHelloWorldSystem implements EntitySystem {

    private static final Pixelfont FONT = Pixelfont.defaultFont(WHITE);

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().position();
        engine.graphics().screen().drawTextCentered(mousePosition, "HELLO WORLD!", FONT, 4);
    }

}
