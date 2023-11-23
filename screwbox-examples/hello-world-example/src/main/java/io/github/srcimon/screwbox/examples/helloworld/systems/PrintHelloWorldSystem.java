package io.github.srcimon.screwbox.examples.helloworld.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;

import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;

public class PrintHelloWorldSystem implements EntitySystem {

    private static final Pixelfont FONT = Pixelfont.defaultFont(WHITE);

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().offset();
        engine.graphics().screen().drawTextCentered(mousePosition, "HELLO WORLD!", FONT, 4);
    }

}
