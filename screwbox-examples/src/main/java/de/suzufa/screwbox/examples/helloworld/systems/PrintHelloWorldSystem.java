package de.suzufa.screwbox.examples.helloworld.systems;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Sprite;

public class PrintHelloWorldSystem implements EntitySystem {

    // TODO: make fontloader
    private final Pixelfont monospaceFont;

    public PrintHelloWorldSystem() {
        monospaceFont = new Pixelfont();
        monospaceFont.addCharacters(
                List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '.',
                        ',', ':', '?', '!'),
                Sprite.multipleFromFile("helloworld/monospace_font.png", Dimension.of(7, 7), 1));
    }

    @Override
    public void update(Engine engine) {
        Offset mousePosition = engine.mouse().position();
        engine.graphics().window().fillWith(Color.WHITE).drawText(mousePosition, "HELLO WORLD!", monospaceFont,
                Percentage.max(), 4);
    }

}
